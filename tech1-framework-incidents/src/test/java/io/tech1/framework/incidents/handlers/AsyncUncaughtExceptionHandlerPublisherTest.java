package io.tech1.framework.incidents.handlers;

import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
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

import java.util.Arrays;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomLong;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AsyncUncaughtExceptionHandlerPublisherTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentPublisher incidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        AsyncUncaughtExceptionHandlerPublisher asyncUncaughtExceptionHandlerPublisher() {
            return new AsyncUncaughtExceptionHandlerPublisher(
                    this.incidentPublisher()
            );
        }
    }

    // Publisher
    private final IncidentPublisher incidentPublisher;

    private final AsyncUncaughtExceptionHandlerPublisher componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.incidentPublisher
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.incidentPublisher
        );
    }

    @Test
    public void handleUncaughtExceptionTest() {
        // Arrange
        var throwable = mock(Throwable.class);
        var method = throwable.getClass().getDeclaredMethods()[0];
        var params = new Object[] { randomString(), randomLong() };

        // Act
        this.componentUnderTest.handleUncaughtException(throwable, method, params);

        // Assert
        verify(this.incidentPublisher).publishThrowable(ThrowableIncident.of(throwable, method, Arrays.asList(params)));
    }
}
