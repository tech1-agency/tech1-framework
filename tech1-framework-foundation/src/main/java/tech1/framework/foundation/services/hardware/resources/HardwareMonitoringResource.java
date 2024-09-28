package tech1.framework.foundation.services.hardware.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringDatapoint;
import tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringMetadata;
import tech1.framework.foundation.services.hardware.publishers.HardwareMonitoringPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static tech1.framework.foundation.utilities.hardware.HardwareUtility.getHeapMemory;

// Swagger
@Tag(name = "[tech1-framework] Hardware API")
// Spring
@Slf4j
@RestController
@RequestMapping("/hardware/monitoring")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HardwareMonitoringResource {

    private final HardwareMonitoringPublisher hardwareMonitoringPublisher;

    @PostMapping("/metadata")
    @ResponseStatus(HttpStatus.OK)
    public void systemMemories(@RequestBody HardwareMonitoringMetadata hardwareMonitoringMetadata) {
        this.hardwareMonitoringPublisher.publishLastHardwareMonitoringDatapoint(
                new EventLastHardwareMonitoringDatapoint(
                        hardwareMonitoringMetadata.version(),
                        new HardwareMonitoringDatapoint(
                                hardwareMonitoringMetadata.systemMemories().global(),
                                hardwareMonitoringMetadata.systemMemories().cpu(),
                                getHeapMemory()
                        )
                )
        );
    }
}
