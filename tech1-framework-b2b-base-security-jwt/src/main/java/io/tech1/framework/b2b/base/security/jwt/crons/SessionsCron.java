package io.tech1.framework.b2b.base.security.jwt.crons;

import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

    @Scheduled(
            cron = "${tech1.securityJwtConfigs.sessionConfigs.enableSessionsMetadataRenewCron.expression}",
            zone = "${tech1.securityJwtConfigs.sessionConfigs.cleanSessionsByExpiredRefreshTokensCron.zoneId}"
    )
    public void enableSessionsMetadataRenew() {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getSessionConfigs().getEnableSessionsMetadataRenewCron().isEnabled()) {
            // WARNING: add session registry business method
        } else {
            LOGGER.debug("Sessions renew metadata cron is disabled");
        }
    }
}
