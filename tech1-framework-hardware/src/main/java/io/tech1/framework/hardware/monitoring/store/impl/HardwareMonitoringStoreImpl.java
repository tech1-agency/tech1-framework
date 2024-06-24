package io.tech1.framework.hardware.monitoring.store.impl;

import io.tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringThresholds;
import io.tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringWidget;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.foundation.domain.base.Version.unknown;
import static io.tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringDatapoint.zeroUsage;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HardwareMonitoringStoreImpl implements HardwareMonitoringStore {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final CircularFifoQueue<EventLastHardwareMonitoringDatapoint> datapoints = new CircularFifoQueue<>(120);

    @Override
    public HardwareMonitoringWidget getHardwareMonitoringWidget() {
        var lastOrUnknownEvent = this.getLastOrUnknownEvent();
        return new HardwareMonitoringWidget(
                lastOrUnknownEvent.version(),
                lastOrUnknownEvent.last().tableView(
                        new HardwareMonitoringThresholds(
                                this.applicationFrameworkProperties.getHardwareMonitoringConfigs().getThresholdsConfigs()
                        )
                )
        );
    }

    @Override
    public boolean containsOneElement() {
        return this.datapoints.size() == 1;
    }

    @Override
    public void storeEvent(EventLastHardwareMonitoringDatapoint event) {
        this.datapoints.add(event);
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private EventLastHardwareMonitoringDatapoint getLastOrUnknownEvent() {
        if (this.datapoints.isEmpty()) {
            return new EventLastHardwareMonitoringDatapoint(unknown(), zeroUsage());
        } else {
            return this.datapoints.get(this.datapoints.size() - 1);
        }
    }
}
