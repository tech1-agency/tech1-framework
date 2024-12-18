package jbst.iam.events.subscribers.websockets;

import jbst.iam.tasks.hardware.HardwareBackPressureTimerTask;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.foundation.services.hardware.store.HardwareMonitoringStore;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class HardwareMonitoringSubscriberWebsocketsTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        HardwareMonitoringStore hardwareMonitoringStore() {
            return mock(HardwareMonitoringStore.class);
        }

        @Bean
        HardwareBackPressureTimerTask hardwareBackPressureTimerTask() {
            return mock(HardwareBackPressureTimerTask.class);
        }

        @Bean
        public IncidentPublisher incidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        HardwareMonitoringSubscriberWebsockets hardwareMonitoringSubscriberWebsockets() {
            return new HardwareMonitoringSubscriberWebsockets(
                    this.hardwareMonitoringStore(),
                    this.hardwareBackPressureTimerTask(),
                    this.incidentPublisher()
            );
        }
    }

    @BeforeEach
    void beforeEach() {
        reset(
                this.hardwareMonitoringStore,
                this.hardwareBackPressureTimerTask,
                this.incidentPublisher
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.hardwareMonitoringStore,
                this.hardwareBackPressureTimerTask,
                this.incidentPublisher
        );
    }

    // Store
    private final HardwareMonitoringStore hardwareMonitoringStore;
    // TimerTasks
    private final HardwareBackPressureTimerTask hardwareBackPressureTimerTask;
    // Incidents
    private final IncidentPublisher incidentPublisher;

    private final HardwareMonitoringSubscriberWebsockets componentUnderTest;

    @Test
    void onLastHardwareMonitoringDatapointExceptionTest() {
        // Arrange
        var event = EventLastHardwareMonitoringDatapoint.random();
        var npe = new NullPointerException("datapoint-exception");
        when(this.hardwareBackPressureTimerTask.isAnyProblemOrFirstDatapoint()).thenThrow(npe);

        // Act
        this.componentUnderTest.onLastHardwareMonitoringDatapoint(event);

        // Assert
        verify(this.hardwareMonitoringStore).storeEvent(event);
        verify(this.hardwareBackPressureTimerTask).isAnyProblemOrFirstDatapoint();
        verify(this.incidentPublisher).publishThrowable(npe);
    }

    @Test
    void onLastHardwareMonitoringDatapointNoProblemNeitherFirstDatapointTest() {
        // Arrange
        var event = EventLastHardwareMonitoringDatapoint.random();
        when(this.hardwareBackPressureTimerTask.isAnyProblemOrFirstDatapoint()).thenReturn(false);

        // Act
        this.componentUnderTest.onLastHardwareMonitoringDatapoint(event);

        // Assert
        verify(this.hardwareMonitoringStore).storeEvent(event);
        verify(this.hardwareBackPressureTimerTask).isAnyProblemOrFirstDatapoint();
    }

    @Test
    void onLastHardwareMonitoringDatapointAnyProblemsTest() {
        // Arrange
        var event = EventLastHardwareMonitoringDatapoint.random();
        when(this.hardwareBackPressureTimerTask.isAnyProblemOrFirstDatapoint()).thenReturn(true);

        // Act
        this.componentUnderTest.onLastHardwareMonitoringDatapoint(event);

        // Assert
        verify(this.hardwareMonitoringStore).storeEvent(event);
        verify(this.hardwareBackPressureTimerTask).isAnyProblemOrFirstDatapoint();
        verify(this.hardwareBackPressureTimerTask).send();
    }
}
