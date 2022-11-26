package io.tech1.framework.hardware.monitoring.subscribers.impl;

import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.hardware.monitoring.subscribers.HardwareMonitoringSubscriber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class BaseHardwareMonitoringSubscriber extends AbstractEventSubscriber implements HardwareMonitoringSubscriber {

    private final HardwareMonitoringStore hardwareMonitoringStore;

    @Override
    public void onLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event) {
        this.hardwareMonitoringStore.storeEvent(event);
    }
}
