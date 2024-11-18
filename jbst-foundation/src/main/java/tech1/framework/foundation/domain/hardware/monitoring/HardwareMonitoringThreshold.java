package tech1.framework.foundation.domain.hardware.monitoring;

import java.math.BigDecimal;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomBigDecimalGreaterThanZeroByBounds;

public record HardwareMonitoringThreshold(
        BigDecimal value
) {
    public static HardwareMonitoringThreshold random() {
        return new HardwareMonitoringThreshold(randomBigDecimalGreaterThanZeroByBounds(50L, 100L));
    }
}
