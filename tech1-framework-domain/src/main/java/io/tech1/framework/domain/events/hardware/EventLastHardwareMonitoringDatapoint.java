package io.tech1.framework.domain.events.hardware;

import io.tech1.framework.domain.base.Version;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringDatapoint;

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
}
