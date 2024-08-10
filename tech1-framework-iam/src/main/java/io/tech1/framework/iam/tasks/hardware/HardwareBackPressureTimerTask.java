package io.tech1.framework.iam.tasks.hardware;

import io.tech1.framework.foundation.domain.concurrent.AbstractInfiniteTimerTask;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.domain.time.SchedulerConfiguration;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import io.tech1.framework.iam.sessions.SessionRegistry;
import io.tech1.framework.iam.template.WssMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static io.tech1.framework.iam.domain.events.WebsocketEvent.hardwareMonitoring;

@Component
public class HardwareBackPressureTimerTask extends AbstractInfiniteTimerTask {

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

    // 60L seconds -> consider add to user settings but on 25.11.2022 no reason to add
    public HardwareBackPressureTimerTask(
            SessionRegistry sessionRegistry,
            WssMessagingTemplate wssMessagingTemplate,
            HardwareMonitoringStore hardwareMonitoringStore,
            IncidentPublisher incidentPublisher,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                new SchedulerConfiguration(60L, 60L, TimeUnit.SECONDS)
        );
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
        if (hardwareConfigs.isEnabled()) {
            var usernames = this.sessionRegistry.getActiveSessionsUsernames();
            var userDestination = hardwareConfigs.getUserDestination();
            usernames.forEach(username -> this.wssMessagingTemplate.sendEventToUser(
                    username,
                    userDestination,
                    hardwareMonitoring(this.hardwareMonitoringStore.getHardwareMonitoringWidget().datapoint())
            ));
        }
    }

    public boolean isAnyProblemOrFirstDatapoint() {
        return this.hardwareMonitoringStore.containsOneElement() || this.hardwareMonitoringStore.getHardwareMonitoringWidget().datapoint().isAnyProblem();
    }
}
