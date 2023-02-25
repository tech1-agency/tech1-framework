package io.tech1.framework.hardware.monitoring.publishers;

import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;

public interface HardwareMonitoringPublisher {
    void publishLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
