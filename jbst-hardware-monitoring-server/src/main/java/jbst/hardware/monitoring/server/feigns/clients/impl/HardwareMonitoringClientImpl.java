package jbst.hardware.monitoring.server.feigns.clients.impl;

import feign.FeignException;
import jbst.foundation.domain.hardware.monitoring.HardwareMonitoringMetadata;
import jbst.hardware.monitoring.server.feigns.clients.HardwareMonitoringClient;
import jbst.hardware.monitoring.server.feigns.definitions.HardwareMonitoringClientDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static jbst.foundation.domain.tuples.TuplePercentage.progressTuplePercentage;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HardwareMonitoringClientImpl implements HardwareMonitoringClient {

    private long iterations = 0L;
    private long successes = 0L;
    private long failures = 0L;

    // Definitions
    private final HardwareMonitoringClientDefinition hardwareMonitoringClientDefinition;

    public void sendHardwareMonitoringMetadata(HardwareMonitoringMetadata hardwareMonitoringMetadata) {
        this.iterations++;
        var lastState = "";
        try {
            this.hardwareMonitoringClientDefinition.sendHardwareMonitoringMetadata(hardwareMonitoringMetadata);
            this.successes++;
            lastState = "200 OK";
        } catch (FeignException ex) {
            this.failures++;
            lastState = "500 ERROR";
        }
        var percentage = progressTuplePercentage(
                new BigDecimal(this.successes),
                new BigDecimal(this.successes + this.failures)
        ).percentage();
        LOGGER.info(
                "Sending hardware metadata...........iteration #{} — {}. Success Rate: {}%",
                this.iterations,
                lastState,
                percentage
        );
    }
}
