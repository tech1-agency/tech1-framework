package jbst.foundation.services.hardware.resources;

import jbst.foundation.configurations.TestRunnerResources;
import jbst.foundation.domain.base.Version;
import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import jbst.foundation.domain.hardware.memories.CpuMemory;
import jbst.foundation.domain.hardware.memories.SystemMemories;
import jbst.foundation.domain.hardware.monitoring.HardwareMonitoringMetadata;
import jbst.foundation.services.hardware.publishers.HardwareMonitoringPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class HardwareMonitoringResourceTest extends TestRunnerResources {

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
                SystemMemories.hardcoded()
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
        assertThat(event.last().getGlobal().a().value()).isEqualTo(new BigDecimal("0.84"));
        assertThat(event.last().getGlobal().a().percentage()).isEqualTo(new BigDecimal("45.6"));
        assertThat(event.last().getGlobal().b().value()).isEqualTo(new BigDecimal("1.00"));
        assertThat(event.last().getGlobal().b().percentage()).isEqualTo(new BigDecimal("60.5"));
        assertThat(event.last().getGlobal().c().value()).isEqualTo(new BigDecimal("1.00"));
        assertThat(event.last().getGlobal().c().percentage()).isEqualTo(new BigDecimal("64.2"));
        assertThat(event.last().getCpu()).isEqualTo(CpuMemory.hardcoded().getValue());
    }
}
