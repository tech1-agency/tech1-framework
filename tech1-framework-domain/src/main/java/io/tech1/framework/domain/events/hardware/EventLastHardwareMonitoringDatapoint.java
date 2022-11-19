package io.tech1.framework.domain.events.hardware;

import io.tech1.framework.domain.base.Version;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringDatapoint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class EventLastHardwareMonitoringDatapoint {
    private final Version version;
    private final HardwareMonitoringDatapoint last;
}
