package jbst.foundation.services.hardware.publishers.impl;

import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import jbst.foundation.services.hardware.publishers.HardwareMonitoringPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class HardwareMonitoringPublisherImplTest {

    @Configuration
    static class ContextConfiguration {
        @Primary
        @Bean
        ApplicationEventPublisher applicationEventPublisher() {
            return mock(ApplicationEventPublisher.class);
        }

        @Bean
        HardwareMonitoringPublisher hardwareMonitoringPublisher() {
            return new HardwareMonitoringPublisherImpl(
                    this.applicationEventPublisher()
            );
        }
    }

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    private final HardwareMonitoringPublisher componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.applicationEventPublisher
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.applicationEventPublisher
        );
    }

    @Test
    void publishLastHardwareMonitoringDatapointTest() {
        // Arrange
        var event = EventLastHardwareMonitoringDatapoint.random();

        // Act
        this.componentUnderTest.publishLastHardwareMonitoringDatapoint(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(event);
    }
}
