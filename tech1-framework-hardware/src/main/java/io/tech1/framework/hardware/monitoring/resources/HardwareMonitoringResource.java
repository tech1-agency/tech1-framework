package io.tech1.framework.hardware.monitoring.resources;

import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringDatapoint;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringMetadata;
import io.tech1.framework.hardware.monitoring.publishers.HardwareMonitoringPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static io.tech1.framework.domain.utilities.hardware.HardwareUtility.getHeapMemory;

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
                        hardwareMonitoringMetadata.getVersion(),
                        new HardwareMonitoringDatapoint(
                                hardwareMonitoringMetadata.getSystemMemories().getGlobal(),
                                hardwareMonitoringMetadata.getSystemMemories().getCpu(),
                                getHeapMemory()
                        )
                )
        );
    }
}
