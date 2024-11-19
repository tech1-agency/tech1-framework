package jbst.hardware.monitoring.server.feigns.definitions;

import feign.Headers;
import feign.RequestLine;
import jbst.foundation.domain.hardware.monitoring.HardwareMonitoringMetadata;
import org.springframework.http.MediaType;

public interface HardwareMonitoringClientDefinition {
    @RequestLine("POST /api/hardware/monitoring/metadata")
    @Headers("Content-Type: " + MediaType.APPLICATION_JSON_VALUE)
    void sendHardwareMonitoringMetadata(HardwareMonitoringMetadata hardwareMonitoringMetadata);
}
