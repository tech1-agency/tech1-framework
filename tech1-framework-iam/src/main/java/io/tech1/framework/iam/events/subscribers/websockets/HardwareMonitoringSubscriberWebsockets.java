package io.tech1.framework.iam.events.subscribers.websockets;

import io.tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import io.tech1.framework.foundation.services.hardware.subscribers.impl.BaseHardwareMonitoringSubscriber;
import io.tech1.framework.iam.tasks.HardwareBackPressureTimerTask;

public class HardwareMonitoringSubscriberWebsockets extends BaseHardwareMonitoringSubscriber {

    // TimerTasks
    private final HardwareBackPressureTimerTask hardwareBackPressureTimerTask;
    // Incidents
    private final IncidentPublisher incidentPublisher;

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
