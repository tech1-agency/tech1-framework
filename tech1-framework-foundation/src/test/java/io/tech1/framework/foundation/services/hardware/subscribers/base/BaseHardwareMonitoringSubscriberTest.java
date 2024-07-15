package io.tech1.framework.foundation.services.hardware.subscribers.base;

import io.tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import io.tech1.framework.foundation.services.hardware.subscribers.HardwareMonitoringSubscriber;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseHardwareMonitoringSubscriberTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        HardwareMonitoringStore hardwareMonitoringStore() {
            return mock(HardwareMonitoringStore.class);
        }

        @Bean
        HardwareMonitoringSubscriber baseHardwareMonitoringSubscriber() {
            return new BaseHardwareMonitoringSubscriber(
                    this.hardwareMonitoringStore()
            );
        }
    }

    private final HardwareMonitoringStore hardwareMonitoringStore;

    private final HardwareMonitoringSubscriber componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.hardwareMonitoringStore
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.hardwareMonitoringStore
        );
    }

    @Test
    void onLastHardwareMonitoringDatapointTest() {
        // Arrange
        var event = EventLastHardwareMonitoringDatapoint.random();

        // Act
        this.componentUnderTest.onLastHardwareMonitoringDatapoint(event);

        // Assert
        verify(this.hardwareMonitoringStore).storeEvent(event);
    }
}
