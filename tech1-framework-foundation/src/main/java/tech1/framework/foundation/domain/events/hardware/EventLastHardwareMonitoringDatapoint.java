package tech1.framework.foundation.domain.events.hardware;

import tech1.framework.foundation.domain.base.Version;
import tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringDatapoint;

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
