package jbst.foundation.services.hardware.publishers;

import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;

public interface HardwareMonitoringPublisher {
    void publishLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event);
}
