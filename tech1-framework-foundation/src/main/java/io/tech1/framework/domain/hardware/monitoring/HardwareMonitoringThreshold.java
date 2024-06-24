package io.tech1.framework.domain.hardware.monitoring;

import java.math.BigDecimal;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBigDecimalGreaterThanZeroByBounds;

public record HardwareMonitoringThreshold(
        BigDecimal value
) {
    public static HardwareMonitoringThreshold random() {
        return new HardwareMonitoringThreshold(randomBigDecimalGreaterThanZeroByBounds(50L, 100L));
    }
}
