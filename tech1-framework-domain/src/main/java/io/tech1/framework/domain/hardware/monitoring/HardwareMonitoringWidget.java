package io.tech1.framework.domain.hardware.monitoring;

import io.tech1.framework.domain.base.Version;

public record HardwareMonitoringWidget(
        Version version,
        HardwareMonitoringDatapointTableView datapoint
) {
}
