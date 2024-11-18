package jbst.foundation.domain.hardware.monitoring;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jbst.foundation.domain.asserts.Asserts.assertNonNullOrThrow;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringThresholds {
    private final Map<HardwareName, HardwareMonitoringThreshold> thresholds;

    public HardwareMonitoringThresholds(
            Map<HardwareName, BigDecimal> thresholds
    ) {
        assertNonNullOrThrow(thresholds, invalidAttribute("HardwareMonitoringThresholds.thresholds"));
        this.thresholds = thresholds.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new HardwareMonitoringThreshold(entry.getValue())
                )
        );
    }

    public static HardwareMonitoringThresholds random() {
        var thresholds = Stream.of(HardwareName.values())
                .collect(
                        Collectors.toMap(
                                entry -> entry,
                                entry -> HardwareMonitoringThreshold.random().value()
                        )
                );
        return new HardwareMonitoringThresholds(thresholds);
    }
}
