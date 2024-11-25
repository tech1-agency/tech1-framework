package jbst.hardware.monitoring.server.client;

import feign.FeignException;
import jbst.foundation.domain.enums.Status;
import jbst.foundation.domain.hardware.monitoring.HardwareMonitoringMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static jbst.foundation.domain.tuples.TuplePercentage.progressTuplePercentage;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HardwareMonitoringClient {

    private long iterations = 0L;
    private long successes = 0L;
    private long failures = 0L;

    // Definitions
    private final HardwareMonitoringClientDefinition hardwareMonitoringClientDefinition;

    @Async
    public void sendHardwareMonitoringMetadata(HardwareMonitoringMetadata hardwareMonitoringMetadata) {
        this.iterations++;
        var status = Status.STARTED;
        try {
            this.hardwareMonitoringClientDefinition.sendHardwareMonitoringMetadata(hardwareMonitoringMetadata);
            this.successes++;
            status = Status.SUCCESS;
        } catch (FeignException ex) {
            this.failures++;
            status = Status.FAILURE;
        }
        var percentage = progressTuplePercentage(
                new BigDecimal(this.successes),
                new BigDecimal(this.successes + this.failures)
        ).percentage();
        LOGGER.info(
                "Sending hardware metadata...........iteration #{} — {}. Success Rate: {}%",
                this.iterations,
                status.formatAnsi(),
                percentage
        );
    }
}
