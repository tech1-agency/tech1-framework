package io.tech1.framework.hardware.monitoring.publishers;

import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import org.springframework.scheduling.annotation.Async;

public interface HardwareMonitoringPublisher {
    @Async
    void publishLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
