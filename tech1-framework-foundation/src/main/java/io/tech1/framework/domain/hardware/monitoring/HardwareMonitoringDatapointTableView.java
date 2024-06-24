package io.tech1.framework.domain.hardware.monitoring;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static org.springframework.util.CollectionUtils.isEmpty;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringDatapointTableView {
    private final List<HardwareMonitoringDatapointTableRow> rows;
    private final boolean anyPresent;
    private final boolean anyProblem;

    public HardwareMonitoringDatapointTableView(
            List<HardwareMonitoringDatapointTableRow> rows
    ) {
        assertNonNullOrThrow(rows, invalidAttribute("HardwareMonitoringDatapointTableView.rows"));
        this.rows = rows;
        this.anyPresent = !isEmpty(this.rows);
        this.anyProblem = rows.stream().anyMatch(HardwareMonitoringDatapointTableRow::isThresholdReached);
    }
}
