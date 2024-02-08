package io.tech1.framework.b2b.base.security.jwt.crons;

import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.crons.AbstractBaseCron;
import io.tech1.framework.domain.utilities.printer.PRINTER;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
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
            cron = "${tech1.securityJwtConfigs.sessionConfigs.cleanSessionsByExpiredRefreshTokensCron.expression}",
            zone = "${tech1.securityJwtConfigs.sessionConfigs.cleanSessionsByExpiredRefreshTokensCron.zoneId}"
    )
    public void cleanByExpiredRefreshTokens() {
        this.executeCron(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getSessionConfigs().getCleanSessionsByExpiredRefreshTokensCron().isEnabled(),
                () -> {
                    var usernames = this.sessionRegistry.getActiveSessionsUsernames();
                    PRINTER.info("Sessions cleanup by expired JWT refresh tokens executed. Active sessions usernames count: `{}`", usernames.size());
                    this.sessionRegistry.cleanByExpiredRefreshTokens(usernames);
                }
        );
    }

    @Scheduled(
            cron = "${tech1.securityJwtConfigs.sessionConfigs.enableSessionsMetadataRenewCron.expression}",
            zone = "${tech1.securityJwtConfigs.sessionConfigs.cleanSessionsByExpiredRefreshTokensCron.zoneId}"
    )
    public void enableSessionsMetadataRenew() {
        this.executeCron(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getSessionConfigs().getEnableSessionsMetadataRenewCron().isEnabled(),
                this.baseUsersSessionsService::enableUserRequestMetadataRenewCron
        );
    }
}
