package tech1.framework.foundation.domain.hardware.monitoring;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;

import static tech1.framework.foundation.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;

class HardwareMonitoringDatapointTableRowTest {

    private static Stream<Arguments> constructorTest() {
        return Stream.of(
                Arguments.of(Map.of(), false),
                Arguments.of(Map.of(HardwareName.HEAP, HardwareMonitoringThreshold.random().value()), false),
                Arguments.of(Map.of(HardwareName.CPU, HardwareMonitoringThreshold.random().value()), false),
                Arguments.of(Map.of(HardwareName.CPU, randomBigDecimalGreaterThanZeroByBounds(5L, 10L)), true)
        );
    }

    @ParameterizedTest
    @MethodSource("constructorTest")
    void constructorTest(Map<HardwareName, BigDecimal> thresholds, boolean expected) {
        // Act
        var actual = new HardwareMonitoringDatapointTableRow(
                HardwareName.CPU,
                randomLongGreaterThanZero(),
                randomBigDecimalGreaterThanZeroByBounds(20L, 30L),
                randomString(),
                new HardwareMonitoringThresholds(thresholds)
        );

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getHardwareName()).isNotNull();
        assertThat(actual.getTimestamp()).isNotZero();
        assertThat(actual.getUsage()).isNotNull();
        assertThat(actual.getValue()).isNotNull();
        assertThat(actual.isThresholdReached()).isEqualTo(expected);
    }
}
