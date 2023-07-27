package io.tech1.framework.b2b.mongodb.security.jwt.crons;

import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionsCron {

    // Sessions
    private final SessionRegistry sessionRegistry;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Scheduled(
            cron = "${tech1.securityJwtConfigs.sessionConfigs.cleanSessionsByExpiredRefreshTokensCron.expression}",
            zone = "${tech1.securityJwtConfigs.sessionConfigs.cleanSessionsByExpiredRefreshTokensCron.zoneId}"
    )
    public void cleanByExpiredRefreshTokens() {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getSessionConfigs().getCleanSessionsByExpiredRefreshTokensCron().isEnabled()) {
            var usernames = this.sessionRegistry.getActiveSessionsUsernames();
            LOGGER.debug("Sessions cleanup by expired JWT refresh tokens executed. Alive users: `{}`", usernames.size());
            this.sessionRegistry.cleanByExpiredRefreshTokens(usernames);
        } else {
            LOGGER.debug("Sessions cleanup by expired JWT refresh tokens is disabled");
        }
    }
}
