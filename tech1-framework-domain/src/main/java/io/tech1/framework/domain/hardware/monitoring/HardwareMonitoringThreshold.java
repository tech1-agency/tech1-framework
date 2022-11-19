package io.tech1.framework.domain.hardware.monitoring;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringThreshold {
    private final BigDecimal value;
}
