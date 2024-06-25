package io.tech1.framework.foundation.domain.hardware.monitoring;

import io.tech1.framework.foundation.domain.base.Version;

public record HardwareMonitoringWidget(
        Version version,
        HardwareMonitoringDatapointTableView datapoint
) {
}
