package jbst.foundation.services.hardware.store;

import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import jbst.foundation.domain.hardware.monitoring.HardwareMonitoringWidget;

public interface HardwareMonitoringStore {
    HardwareMonitoringWidget getHardwareMonitoringWidget();
    boolean containsOneElement();
    void storeEvent(EventLastHardwareMonitoringDatapoint event);
}
