package tech1.framework.foundation.incidents.handlers;

import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RejectedExecutionHandlerPublisherTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentPublisher incidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        RejectedExecutionHandlerPublisher rejectedExecutionHandlerPublisher() {
            return new RejectedExecutionHandlerPublisher(
                    this.incidentPublisher()
            );
        }
    }

    // Publisher
    private final IncidentPublisher incidentPublisher;

    private final RejectedExecutionHandlerPublisher componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.incidentPublisher
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.incidentPublisher
        );
    }

    @Test
    void rejectedExecutionTest() {
        // Arrange
        var runnableName = randomString();
        var executorName = randomString();
        var runnable = mock(Runnable.class);
        when(runnable.toString()).thenReturn(runnableName);
        var executor = mock(ThreadPoolExecutor.class);
        when(executor.toString()).thenReturn(executorName);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.rejectedExecution(runnable, executor));

        // Assert
        var message = "Task " + runnableName + " rejected from " + executorName;
        var exceptionAC = ArgumentCaptor.forClass(RejectedExecutionException.class);
        verify(this.incidentPublisher).publishThrowable(exceptionAC.capture());
        assertThat(exceptionAC.getValue().getMessage()).isEqualTo(message);
        assertThat(throwable).isNotNull();
        assertThat(throwable.getClass()).isEqualTo(RejectedExecutionException.class);
        assertThat(throwable.getMessage()).isEqualTo(message);
    }
}
