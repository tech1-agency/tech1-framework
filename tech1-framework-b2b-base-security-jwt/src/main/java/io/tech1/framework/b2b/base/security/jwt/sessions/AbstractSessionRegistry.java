package io.tech1.framework.b2b.base.security.jwt.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLogin;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionRefreshed;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.domain.base.Username;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.SESSION_REGISTRY_REGISTER_SESSION;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.SESSION_REGISTRY_RENEW_SESSION;

@Slf4j
public abstract class AbstractSessionRegistry implements SessionRegistry {

    protected final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    protected final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    protected final BaseUsersSessionsService baseUsersSessionsService;

    protected AbstractSessionRegistry(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            BaseUsersSessionsService baseUsersSessionsService
    ) {
        this.securityJwtPublisher = securityJwtPublisher;
        this.securityJwtIncidentPublisher = securityJwtIncidentPublisher;
        this.baseUsersSessionsService = baseUsersSessionsService;
    }

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
}
