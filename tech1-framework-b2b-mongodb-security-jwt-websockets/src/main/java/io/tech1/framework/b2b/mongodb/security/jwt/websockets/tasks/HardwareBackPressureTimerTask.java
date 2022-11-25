package io.tech1.framework.b2b.mongodb.security.jwt.websockets.tasks;

import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.websockets.template.WssMessagingTemplate;
import io.tech1.framework.domain.concurrent.AbstractTimerTask;
import io.tech1.framework.domain.time.SchedulerConfiguration;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static io.tech1.framework.b2b.mongodb.security.jwt.websockets.domain.events.WebsocketEvent.hardwareMonitoring;

@Slf4j
@Component
public class HardwareBackPressureTimerTask extends AbstractTimerTask {

    // Sessions
    private final SessionRegistry sessionRegistry;
    // Wss
    private final WssMessagingTemplate wssMessagingTemplate;
    // Store
    private final HardwareMonitoringStore hardwareMonitoringStore;
    // Publishers
    private final IncidentPublisher incidentPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    // WARNING: 60L seconds -> consider add to user settings but on 25.11.2022 no reason to add
    @Autowired
    protected HardwareBackPressureTimerTask(
            SessionRegistry sessionRegistry,
            WssMessagingTemplate wssMessagingTemplate,
            HardwareMonitoringStore hardwareMonitoringStore,
            IncidentPublisher incidentPublisher,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(SchedulerConfiguration.of(60L, 60L, TimeUnit.SECONDS));
        this.sessionRegistry = sessionRegistry;
        this.wssMessagingTemplate = wssMessagingTemplate;
        this.hardwareMonitoringStore = hardwareMonitoringStore;
        this.incidentPublisher = incidentPublisher;
        this.applicationFrameworkProperties = applicationFrameworkProperties;

        var hardwareConfigs = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getFeaturesConfigs().getHardwareConfigs();
        boolean hardwareConfigsEnabled = hardwareConfigs.isEnabled();
        if (hardwareConfigsEnabled) {
            this.start();
        }
    }

    @Override
    public void onTick() {
        try {
            this.send();
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }
    }

    public void send() {
        var hardwareConfigs = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getFeaturesConfigs().getHardwareConfigs();
        boolean hardwareConfigsEnabled = hardwareConfigs.isEnabled();
        if (hardwareConfigsEnabled) {
            var usernames = this.sessionRegistry.getActiveSessionsUsernames();
            var userDestination = hardwareConfigs.getUserDestination();
            usernames.forEach(username -> this.wssMessagingTemplate.sendEventToUser(
                    username,
                    userDestination,
                    hardwareMonitoring(this.hardwareMonitoringStore.getHardwareMonitoringWidget().getDatapoint())
            ));
        }
    }

    public boolean isAnyProblemOrFirstDatapoint() {
        return this.hardwareMonitoringStore.getHardwareMonitoringWidget().getDatapoint().isAnyProblem() ||
                this.hardwareMonitoringStore.containsOneElement();
    }
}
