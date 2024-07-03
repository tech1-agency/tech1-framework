package io.tech1.framework.foundation.services.hardware.store;

import io.tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringWidget;

public interface HardwareMonitoringStore {
    HardwareMonitoringWidget getHardwareMonitoringWidget();
    boolean containsOneElement();
    void storeEvent(EventLastHardwareMonitoringDatapoint event);
}
