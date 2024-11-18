package tech1.framework.foundation.services.hardware.publishers;

import tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;

public interface HardwareMonitoringPublisher {
    void publishLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
