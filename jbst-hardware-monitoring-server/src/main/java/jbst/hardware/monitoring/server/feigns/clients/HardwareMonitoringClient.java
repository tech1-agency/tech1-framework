package jbst.hardware.monitoring.server.feigns.clients;

import jbst.foundation.domain.hardware.monitoring.HardwareMonitoringMetadata;
import org.springframework.scheduling.annotation.Async;

public interface HardwareMonitoringClient {
    @Async
    void sendHardwareMonitoringMetadata(HardwareMonitoringMetadata hardwareMonitoringMetadata);
}
