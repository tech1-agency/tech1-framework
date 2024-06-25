package io.tech1.framework.b2b.base.security.jwt.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLogin;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLogout;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionExpired;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionRefreshed;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.tech1.framework.foundation.domain.constants.FrameworkLogsConstants.*;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractSessionRegistry implements SessionRegistry {

    protected final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    protected final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    protected final BaseUsersSessionsService baseUsersSessionsService;
    // Repositories
    protected final UsersSessionsRepository usersSessionsRepository;

    @Override
    public Set<String> getActiveSessionsUsernamesIdentifiers() {
        return this.sessions.stream()
                .map(session -> session.username().value())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Username> getActiveSessionsUsernames() {
        return this.sessions.stream()
                .map(Session::username)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<JwtAccessToken> getActiveSessionsAccessTokens() {
        return this.sessions.stream()
                .map(Session::accessToken)
                .collect(Collectors.toSet());
    }

    @Override
    public void register(Session session) {
        var username = session.username();
        boolean added = this.sessions.add(session);
        if (added) {
            LOGGER.debug(SESSION_REGISTRY_REGISTER_SESSION, username);
            this.securityJwtPublisher.publishAuthenticationLogin(new EventAuthenticationLogin(username));
        }
    }

    @Override
    public void renew(Username username, JwtRefreshToken oldRefreshToken, JwtAccessToken newAccessToken, JwtRefreshToken newRefreshToken) {
        this.sessions.removeIf(session -> session.refreshToken().equals(oldRefreshToken));
        var newSession = new Session(username, newAccessToken, newRefreshToken);
        var added = this.sessions.add(newSession);
        if (added) {
            LOGGER.debug(SESSION_REGISTRY_RENEW_SESSION, username);
            this.securityJwtPublisher.publishSessionRefreshed(new EventSessionRefreshed(newSession));
        }
    }

    @Override
    public void logout(Username username, JwtAccessToken accessToken) {
        LOGGER.debug(SESSION_REGISTRY_REMOVE_SESSION, username);
        var removed = this.sessions.removeIf(session -> session.accessToken().equals(accessToken));
        if (removed) {
            this.securityJwtPublisher.publishAuthenticationLogout(new EventAuthenticationLogout(username));

            var sessionTP = this.usersSessionsRepository.isPresent(accessToken);

            if (sessionTP.present()) {
                var session = sessionTP.value();
                this.securityJwtIncidentPublisher.publishAuthenticationLogoutFull(new IncidentAuthenticationLogoutFull(username, session.metadata()));
                this.usersSessionsRepository.delete(session.id());
            } else {
                this.securityJwtIncidentPublisher.publishAuthenticationLogoutMin(new IncidentAuthenticationLogoutMin(username));
            }
        }

    }

    @Override
    public void cleanByExpiredRefreshTokens(Set<Username> usernames) {
        var sessionsValidatedTuple2 = this.baseUsersSessionsService.getExpiredRefreshTokensSessions(usernames);

        sessionsValidatedTuple2.expiredSessions().forEach(tuple -> {
            var username = tuple.a();
            var refreshToken = tuple.b();
            var metadata = tuple.c();

            LOGGER.debug(SESSION_REGISTRY_EXPIRE_SESSION, username);
            var sessionOpt = this.sessions.stream()
                    .filter(session -> session.refreshToken().equals(refreshToken))
                    .findFirst();

            if (sessionOpt.isPresent()) {
                var session = sessionOpt.get();
                this.sessions.remove(session);
                this.securityJwtPublisher.publishSessionExpired(new EventSessionExpired(session));
                this.securityJwtIncidentPublisher.publishSessionExpired(new IncidentSessionExpired(username, metadata));
            }
        });

        var deleted = this.usersSessionsRepository.delete(sessionsValidatedTuple2.expiredOrInvalidSessionIds());
        LOGGER.debug("JWT expired or invalid refresh tokens ids was successfully deleted. Count: `{}`", deleted);
    }

    @Override
    public ResponseUserSessionsTable getSessionsTable(Username username, RequestAccessToken requestAccessToken) {
        return ResponseUserSessionsTable.of(this.usersSessionsRepository.getUsersSessionsTable(username, requestAccessToken));
    }
}
