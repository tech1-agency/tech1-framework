package tech1.framework.foundation.services.hardware.subscribers;

import tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import org.springframework.context.event.EventListener;

public interface HardwareMonitoringSubscriber {
    @EventListener
    void onLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
