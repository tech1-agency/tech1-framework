package io.tech1.framework.b2b.mongodb.security.jwt.sessions.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventAuthenticationLogin;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventAuthenticationLogout;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionExpired;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionRefreshed;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.Session;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.constants.FrameworkLogsConstants;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionRegistryImpl implements SessionRegistry {

    private final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UserSessionService userSessionService;

    @Override
    public Set<String> getActiveSessionsUsernamesIdentifiers() {
        return this.sessions.stream()
                .map(session -> session.getUsername().getIdentifier())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Username> getActiveSessionsUsernames() {
        return this.sessions.stream()
                .map(Session::getUsername)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<JwtRefreshToken> getActiveSessionsRefreshTokens() {
        return this.sessions.stream()
                .map(Session::getRefreshToken)
                .collect(Collectors.toSet());
    }

    @Override
    public void register(Session session) {
        var username = session.getUsername();
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
            var username = newSession.getUsername();
            LOGGER.debug(SESSION_REGISTRY_RENEW_SESSION, username);

            this.securityJwtPublisher.publishSessionRefreshed(new EventSessionRefreshed(newSession));
        }
    }

    @Override
    public void logout(Session session) {
        var username = session.getUsername();
        LOGGER.debug(SESSION_REGISTRY_REMOVE_SESSION, username);
        this.sessions.remove(session);

        this.securityJwtPublisher.publishAuthenticationLogout(new EventAuthenticationLogout(session));

        var jwtRefreshToken = session.getRefreshToken();
        var dbUserSession = this.userSessionService.findByRefreshToken(jwtRefreshToken);

        if (nonNull(dbUserSession)) {
            this.securityJwtIncidentPublisher.publishAuthenticationLogoutFull(IncidentAuthenticationLogoutFull.of(username, dbUserSession.getRequestMetadata()));
            this.userSessionService.deleteByRefreshToken(jwtRefreshToken);
        } else {
            this.securityJwtIncidentPublisher.publishAuthenticationLogoutMin(IncidentAuthenticationLogoutMin.of(username));
        }
    }

    @Override
    public void cleanByExpiredRefreshTokens(List<DbUserSession> usersSessions) {
        var sessionsValidatedTuple2 = this.userSessionService.validate(usersSessions);

        sessionsValidatedTuple2.getExpiredSessions().forEach(tuple2 -> {
            var username = tuple2.getA();
            var dbUserSession = tuple2.getB();
            var session = new Session(username, dbUserSession.getJwtRefreshToken());
            LOGGER.debug(FrameworkLogsConstants.SESSION_REGISTRY_EXPIRE_SESSION, username);
            this.sessions.remove(session);
            this.securityJwtPublisher.publishSessionExpired(new EventSessionExpired(session));
            this.securityJwtIncidentPublisher.publishSessionExpired(IncidentSessionExpired.of(username, dbUserSession.getRequestMetadata()));
        });

        var deleted = this.userSessionService.deleteByIdIn(sessionsValidatedTuple2.getExpiredOrInvalidSessionIds());
        LOGGER.debug("JWT expired or invalid refresh tokens ids was successfully deleted. Count: `{}`", deleted);
    }
}
