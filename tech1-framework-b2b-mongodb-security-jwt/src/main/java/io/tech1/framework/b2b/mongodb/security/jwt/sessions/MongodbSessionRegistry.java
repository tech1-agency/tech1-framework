package io.tech1.framework.b2b.mongodb.security.jwt.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventAuthenticationLogin;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventAuthenticationLogout;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionExpired;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionRefreshed;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongodbSessionRegistry implements SessionRegistry {

    private final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UserSessionService userSessionService;

    @Override
    public Set<String> getActiveSessionsUsernamesIdentifiers() {
        return this.sessions.stream()
                .map(session -> session.username().identifier())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Username> getActiveSessionsUsernames() {
        return this.sessions.stream()
                .map(Session::username)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<JwtRefreshToken> getActiveSessionsRefreshTokens() {
        return this.sessions.stream()
                .map(Session::refreshToken)
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
    public void renew(Session oldSession, Session newSession) {
        this.sessions.remove(oldSession);
        boolean added = this.sessions.add(newSession);
        if (added) {
            var username = newSession.username();
            LOGGER.debug(SESSION_REGISTRY_RENEW_SESSION, username);

            this.securityJwtPublisher.publishSessionRefreshed(new EventSessionRefreshed(newSession));
        }
    }

    @Override
    public void logout(Session session) {
        var username = session.username();
        LOGGER.debug(SESSION_REGISTRY_REMOVE_SESSION, username);
        this.sessions.remove(session);

        this.securityJwtPublisher.publishAuthenticationLogout(new EventAuthenticationLogout(session));

        var jwtRefreshToken = session.refreshToken();
        var dbUserSession = this.userSessionService.findByRefreshToken(jwtRefreshToken);

        if (nonNull(dbUserSession)) {
            this.securityJwtIncidentPublisher.publishAuthenticationLogoutFull(new IncidentAuthenticationLogoutFull(username, dbUserSession.getRequestMetadata()));
            this.userSessionService.deleteByRefreshToken(jwtRefreshToken);
        } else {
            this.securityJwtIncidentPublisher.publishAuthenticationLogoutMin(new IncidentAuthenticationLogoutMin(username));
        }
    }

    @Override
    public void cleanByExpiredRefreshTokens(Set<Username> usernames) {
        var usersSessions = this.userSessionService.findByUsernameIn(usernames);
        var sessionsValidatedTuple2 = this.userSessionService.validate(usersSessions);

        sessionsValidatedTuple2.expiredSessions().forEach(tuple2 -> {
            var username = tuple2.a();
            var requestMetadata = tuple2.b();
            var jwtRefreshToken = tuple2.c();
            var session = new Session(username, jwtRefreshToken);
            LOGGER.debug(SESSION_REGISTRY_EXPIRE_SESSION, username);
            this.sessions.remove(session);
            this.securityJwtPublisher.publishSessionExpired(new EventSessionExpired(session));
            this.securityJwtIncidentPublisher.publishSessionExpired(new IncidentSessionExpired(username, requestMetadata));
        });

        var deleted = this.userSessionService.deleteByIdIn(sessionsValidatedTuple2.expiredOrInvalidSessionIds());
        LOGGER.debug("JWT expired or invalid refresh tokens ids was successfully deleted. Count: `{}`", deleted);
    }

    @Override
    public ResponseUserSessionsTable getSessionsTable(Username username, CookieRefreshToken cookie) {
        return ResponseUserSessionsTable.of(
                this.userSessionService.findByUsername(username).stream()
                        .map(session ->
                                ResponseUserSession2.of(
                                        session.getUsername(),
                                        session.getRequestMetadata(),
                                        session.getJwtRefreshToken(),
                                        cookie
                                )
                        )
                        .collect(Collectors.toList())
        );
    }
}
