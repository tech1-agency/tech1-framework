package jbst.foundation.services.hardware.subscribers;

import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import org.springframework.context.event.EventListener;

public interface HardwareMonitoringSubscriber {
    @EventListener
    void onLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
