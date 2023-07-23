package io.tech1.framework.hardware.monitoring.resources;

import io.tech1.framework.domain.base.Version;
import io.tech1.framework.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.domain.hardware.memories.SystemMemories;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringMetadata;
import io.tech1.framework.domain.tests.constants.TestsHardwareConstants;
import io.tech1.framework.hardware.monitoring.publishers.HardwareMonitoringPublisher;
import io.tech1.framework.hardware.tests.runners.ApplicationResourceRunner;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static io.tech1.framework.domain.tests.constants.TestsHardwareConstants.CPU_MEMORY;
import static io.tech1.framework.domain.tests.constants.TestsHardwareConstants.GLOBAL_MEMORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class HardwareMonitoringResourceTest extends ApplicationResourceRunner {

    private final HardwareMonitoringPublisher hardwareMonitoringPublisher;

    private final HardwareMonitoringResource resourceUnderTest;

    @BeforeEach
    void beforeEach() {
        beforeByResource(this.resourceUnderTest);
        reset(
                this.hardwareMonitoringPublisher
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.hardwareMonitoringPublisher
        );
    }

    @Test
    void systemMemoriesTest() throws Exception {
        // Arrange
        var hardwareMonitoringMetadata = new HardwareMonitoringMetadata(
                Version.unknown(),
                new SystemMemories(GLOBAL_MEMORY, TestsHardwareConstants.CPU_MEMORY)
        );

        // Act
        mvc.perform(
                post("/hardware/monitoring/metadata")
                        .content(this.asJsonString(hardwareMonitoringMetadata))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        var eventAC = ArgumentCaptor.forClass(EventLastHardwareMonitoringDatapoint.class);
        verify(this.hardwareMonitoringPublisher).publishLastHardwareMonitoringDatapoint(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.version()).isEqualTo(Version.unknown());
        assertThat(event.last().getGlobal().getA().getValue()).isEqualTo(new BigDecimal("0.84"));
        assertThat(event.last().getGlobal().getA().getPercentage()).isEqualTo(new BigDecimal("45.6"));
        assertThat(event.last().getGlobal().getB().getValue()).isEqualTo(new BigDecimal("1.00"));
        assertThat(event.last().getGlobal().getB().getPercentage()).isEqualTo(new BigDecimal("60.5"));
        assertThat(event.last().getGlobal().getC().getValue()).isEqualTo(new BigDecimal("1.00"));
        assertThat(event.last().getGlobal().getC().getPercentage()).isEqualTo(new BigDecimal("64.2"));
        assertThat(event.last().getCpu()).isEqualTo(CPU_MEMORY.value());
    }
}
