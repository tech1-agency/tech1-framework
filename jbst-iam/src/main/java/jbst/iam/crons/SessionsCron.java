package jbst.iam.crons;

import jbst.iam.services.BaseUsersSessionsService;
import jbst.iam.sessions.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jbst.foundation.domain.crons.AbstractBaseCron;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionsCron extends AbstractBaseCron {

    // Sessions
    private final SessionRegistry sessionRegistry;
    // Services
    private final BaseUsersSessionsService baseUsersSessionsService;
    // Incidents
    private final IncidentPublisher incidentPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void processException(Exception ex) {
        this.incidentPublisher.publishThrowable(ex);
    }

    @Scheduled(
            cron = "${tech1.security-jwt-configs.session-configs.clean-sessions-by-expired-refresh-tokens-cron.expression}",
            zone = "${tech1.security-jwt-configs.session-configs.clean-sessions-by-expired-refresh-tokens-cron.zone-id}"
    )
    public void cleanByExpiredRefreshTokens() {
        this.executeCron(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getSessionConfigs().getCleanSessionsByExpiredRefreshTokensCron().isEnabled(),
                () -> {
                    var usernames = this.sessionRegistry.getActiveSessionsUsernames();
                    LOGGER.info("Sessions cleanup by expired JWT refresh tokens executed. Active sessions usernames count: `{}`", usernames.size());
                    this.sessionRegistry.cleanByExpiredRefreshTokens(usernames);
                }
        );
    }

    @Scheduled(
            cron = "${tech1.security-jwt-configs.session-configs.enable-sessions-metadata-renew-cron.expression}",
            zone = "${tech1.security-jwt-configs.session-configs.enable-sessions-metadata-renew-cron.zone-id}"
    )
    public void enableSessionsMetadataRenew() {
        this.executeCron(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getSessionConfigs().getEnableSessionsMetadataRenewCron().isEnabled(),
                this.baseUsersSessionsService::enableUserRequestMetadataRenewCron
        );
    }
}
