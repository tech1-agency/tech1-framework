package jbst.foundation.domain.events.hardware;

import jbst.foundation.domain.base.Version;
import jbst.foundation.domain.hardware.monitoring.HardwareMonitoringDatapoint;

public record EventLastHardwareMonitoringDatapoint(
        Version version,
        HardwareMonitoringDatapoint last
) {
    public static EventLastHardwareMonitoringDatapoint random() {
        return new EventLastHardwareMonitoringDatapoint(
                Version.random(),
                HardwareMonitoringDatapoint.random()
        );
    }

    public static EventLastHardwareMonitoringDatapoint unknownVersionZeroUsage() {
        return new EventLastHardwareMonitoringDatapoint(
                Version.unknown(),
                HardwareMonitoringDatapoint.zeroUsage()
        );
    }
}
