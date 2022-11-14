package io.tech1.framework.domain.hardware.monitoring;

import io.tech1.framework.domain.base.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringWidget {
    private final Version version;
    private final HardwareMonitoringDatapointTableView datapoint;
}
