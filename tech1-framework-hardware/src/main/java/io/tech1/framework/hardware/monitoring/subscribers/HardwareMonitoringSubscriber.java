package io.tech1.framework.hardware.monitoring.subscribers;

import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import org.springframework.context.event.EventListener;

public interface HardwareMonitoringSubscriber {
    @EventListener
    void onLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
