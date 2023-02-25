package io.tech1.framework.hardware.monitoring.subscribers;

import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface HardwareMonitoringSubscriber {
    @Async
    @EventListener
    void onLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
