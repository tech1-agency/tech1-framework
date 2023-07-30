package io.tech1.framework.b2b.postgres.security.jwt.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLogout;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionExpired;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.sessions.AbstractSessionRegistry;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.SESSION_REGISTRY_EXPIRE_SESSION;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.SESSION_REGISTRY_REMOVE_SESSION;
import static java.util.Objects.nonNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class PostgresSessionRegistry extends AbstractSessionRegistry {

    private final PostgresUsersSessionsRepository postgresUsersSessionsRepository;

    @Autowired
    public PostgresSessionRegistry(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            BaseUsersSessionsService baseUsersSessionsService,
            PostgresUsersSessionsRepository postgresUsersSessionsRepository
    ) {
        super(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                baseUsersSessionsService
        );
        this.postgresUsersSessionsRepository = postgresUsersSessionsRepository;
    }

    @Override
    public void logout(Session session) {
        var username = session.username();
        LOGGER.debug(SESSION_REGISTRY_REMOVE_SESSION, username);
        this.sessions.remove(session);

        this.securityJwtPublisher.publishAuthenticationLogout(new EventAuthenticationLogout(session));

        var jwtRefreshToken = session.refreshToken();
        var dbUserSession = this.postgresUsersSessionsRepository.findByRefreshToken(jwtRefreshToken);

        if (nonNull(dbUserSession)) {
            this.securityJwtIncidentPublisher.publishAuthenticationLogoutFull(new IncidentAuthenticationLogoutFull(username, dbUserSession.getMetadata()));
            this.postgresUsersSessionsRepository.deleteByRefreshToken(jwtRefreshToken);
        } else {
            this.securityJwtIncidentPublisher.publishAuthenticationLogoutMin(new IncidentAuthenticationLogoutMin(username));
        }
    }

    @Override
    public void cleanByExpiredRefreshTokens(Set<Username> usernames) {
        var sessionsValidatedTuple2 = this.baseUsersSessionsService.getExpiredSessions(usernames);

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

        var deleted = this.postgresUsersSessionsRepository.deleteByIdIn(sessionsValidatedTuple2.expiredOrInvalidSessionIds());
        LOGGER.debug("JWT expired or invalid refresh tokens ids was successfully deleted. Count: `{}`", deleted);
    }

    @Override
    public ResponseUserSessionsTable getSessionsTable(Username username, CookieRefreshToken cookie) {
        return ResponseUserSessionsTable.of(
                this.postgresUsersSessionsRepository.findByUsername(username).stream()
                        .map(session -> session.responseUserSession2(cookie))
                        .collect(Collectors.toList())
        );
    }
}
