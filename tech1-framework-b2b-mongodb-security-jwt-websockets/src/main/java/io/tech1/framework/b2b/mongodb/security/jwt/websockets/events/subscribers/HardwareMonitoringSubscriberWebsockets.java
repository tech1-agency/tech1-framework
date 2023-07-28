package io.tech1.framework.b2b.mongodb.security.jwt.websockets.events.subscribers;

import io.tech1.framework.b2b.mongodb.security.jwt.websockets.tasks.HardwareBackPressureTimerTask;
import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.hardware.monitoring.subscribers.impl.BaseHardwareMonitoringSubscriber;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
public class HardwareMonitoringSubscriberWebsockets extends BaseHardwareMonitoringSubscriber {

    // TimerTasks
    private final HardwareBackPressureTimerTask hardwareBackPressureTimerTask;
    // Incidents
    private final IncidentPublisher incidentPublisher;

    @Autowired
    public HardwareMonitoringSubscriberWebsockets(
            HardwareMonitoringStore hardwareMonitoringStore,
            HardwareBackPressureTimerTask hardwareBackPressureTimerTask,
            IncidentPublisher incidentPublisher
    ) {
        super(
                hardwareMonitoringStore
        );
        this.hardwareBackPressureTimerTask = hardwareBackPressureTimerTask;
        this.incidentPublisher = incidentPublisher;
    }

    @Override
    public void onLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event) {
        try {
            super.onLastHardwareMonitoringDatapoint(event);
            if (this.hardwareBackPressureTimerTask.isAnyProblemOrFirstDatapoint()) {
                this.hardwareBackPressureTimerTask.send();
            }
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }
    }
}
