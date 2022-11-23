package io.tech1.framework.domain.hardware.monitoring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.numbers.BigDecimalUtility.isFirstValueGreater;
import static org.springframework.util.CollectionUtils.isEmpty;

// Lombok
@Getter
@EqualsAndHashCode(exclude = {
        "timestamp"
})
@ToString
public class HardwareMonitoringDatapointTableRow {
    private final HardwareName hardwareName;
    private final long timestamp;
    private final BigDecimal usage;
    private final String value;

    @JsonIgnore
    private final boolean thresholdReached;

    public HardwareMonitoringDatapointTableRow(
            HardwareName hardwareName,
            long timestamp,
            BigDecimal usage,
            String value,
            HardwareMonitoringThresholds thresholds
    ) {
        assertNonNullOrThrow(hardwareName, invalidAttribute("HardwareMonitoringDatapointTableRow.hardwareName"));
        assertNonNullOrThrow(usage, invalidAttribute("HardwareMonitoringDatapointTableRow.usage"));
        assertNonNullOrThrow(value, invalidAttribute("HardwareMonitoringDatapointTableRow.value"));
        assertNonNullOrThrow(thresholds, invalidAttribute("HardwareMonitoringDatapointTableRow.thresholds"));
        this.hardwareName = hardwareName;
        this.timestamp = timestamp;
        this.usage = usage;
        this.value = value;
        if (!isEmpty(thresholds.getThresholds()) && thresholds.getThresholds().containsKey(hardwareName)) {
            this.thresholdReached = isFirstValueGreater(usage, thresholds.getThresholds().get(hardwareName).getValue());
        } else {
            this.thresholdReached = false;
        }
    }
}
