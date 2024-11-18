package jbst.foundation.domain.hardware.monitoring;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringDatapointTableView {
    private final List<HardwareMonitoringDatapointTableRow> rows;
    private final boolean anyPresent;
    private final boolean anyProblem;

    public HardwareMonitoringDatapointTableView(@NotNull List<HardwareMonitoringDatapointTableRow> rows) {
        this.rows = rows;
        this.anyPresent = !isEmpty(this.rows);
        this.anyProblem = rows.stream().anyMatch(HardwareMonitoringDatapointTableRow::isThresholdReached);
    }
}
