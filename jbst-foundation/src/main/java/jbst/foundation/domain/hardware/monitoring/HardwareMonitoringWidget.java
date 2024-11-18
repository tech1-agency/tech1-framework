package jbst.foundation.domain.hardware.monitoring;

import jbst.foundation.domain.base.Version;
import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import jbst.foundation.domain.properties.configs.HardwareMonitoringConfigs;

public record HardwareMonitoringWidget(
        Version version,
        HardwareMonitoringDatapointTableView datapoint
) {

    public static HardwareMonitoringWidget of(EventLastHardwareMonitoringDatapoint event, HardwareMonitoringConfigs configs) {
        return new HardwareMonitoringWidget(
                event.version(),
                event.last().tableView(
                        new HardwareMonitoringThresholds(
                               configs.getThresholdsConfigs()
                        )
                )
        );
    }
}
