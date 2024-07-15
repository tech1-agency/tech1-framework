package io.tech1.framework.foundation.services.hardware.store.impl;

import io.tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringWidget;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HardwareMonitoringStoreImpl implements HardwareMonitoringStore {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final Deque<EventLastHardwareMonitoringDatapoint> datapoints = new ConcurrentLinkedDeque<>();

    @Override
    public HardwareMonitoringWidget getHardwareMonitoringWidget() {
        return HardwareMonitoringWidget.of(
                this.getLastOrUnknownEvent(),
                this.applicationFrameworkProperties.getHardwareMonitoringConfigs()
        );
    }

    @Override
    public boolean containsOneElement() {
        return this.datapoints.size() == 1;
    }

    @Override
    public void storeEvent(EventLastHardwareMonitoringDatapoint event) {
        if (this.datapoints.size() >= 120) {
            this.datapoints.pollFirst();
        }
        this.datapoints.offerLast(event);
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private EventLastHardwareMonitoringDatapoint getLastOrUnknownEvent() {
        if (!isEmpty(this.datapoints)) {
            return this.datapoints.peekLast();
        } else {
            return EventLastHardwareMonitoringDatapoint.unknownVersionZeroUsage();
        }
    }
}
