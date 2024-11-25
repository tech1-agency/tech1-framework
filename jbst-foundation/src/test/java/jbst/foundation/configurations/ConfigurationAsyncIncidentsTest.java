package jbst.foundation.configurations;

import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.incidents.handlers.AsyncUncaughtExceptionHandlerPublisher;
import jbst.foundation.incidents.handlers.RejectedExecutionHandlerPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jbst.foundation.utilities.processors.ProcessorsUtility.getNumOfCores;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ConfigurationAsyncIncidentsTest {

    @Configuration
    @Import({
            TestConfigurationPropertiesJbstHardcoded.class,
            ConfigurationAsyncIncidents.class
    })
    static class ContextConfiguration {
        @Bean
        AsyncUncaughtExceptionHandlerPublisher asyncUncaughtExceptionHandlerPublisher() {
            return mock(AsyncUncaughtExceptionHandlerPublisher.class);
        }

        @Bean
        RejectedExecutionHandlerPublisher rejectedExecutionHandlerPublisher() {
            return mock(RejectedExecutionHandlerPublisher.class);
        }
    }

    // Exceptions
    private final AsyncUncaughtExceptionHandlerPublisher asyncUncaughtExceptionHandlerPublisher;
    private final RejectedExecutionHandlerPublisher rejectedExecutionHandlerPublisher;
    // Properties
    private final JbstProperties jbstProperties;

    private final ConfigurationAsyncIncidents componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.asyncUncaughtExceptionHandlerPublisher,
                this.rejectedExecutionHandlerPublisher
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.asyncUncaughtExceptionHandlerPublisher,
                this.rejectedExecutionHandlerPublisher
        );
    }

    @Test
    void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods).contains("getAsyncExecutor");
        assertThat(methods).contains("getAsyncUncaughtExceptionHandler");
        assertThat(methods).hasSize(16);
    }

    @Test
    void getAsyncExecutorTest() {
        // Arrange
        var asyncConfigs = this.jbstProperties.getAsyncConfigs();

        // Act
        var actual = this.componentUnderTest.getAsyncExecutor();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getClass()).isEqualTo(ThreadPoolTaskExecutor.class);
        var threadPoolTaskExecutor = (ThreadPoolTaskExecutor) actual;
        assertThat(threadPoolTaskExecutor.getThreadNamePrefix()).isEqualTo(asyncConfigs.getThreadNamePrefix());
        assertThat(threadPoolTaskExecutor.getCorePoolSize()).isEqualTo(getNumOfCores(asyncConfigs.asThreadsCorePoolTuplePercentage()));
        assertThat(threadPoolTaskExecutor.getMaxPoolSize()).isEqualTo(getNumOfCores(asyncConfigs.asThreadsMaxPoolTuplePercentage()));
    }

    @Test
    void getAsyncUncaughtExceptionHandlerTest() {
        // Act
        var actual = this.componentUnderTest.getAsyncUncaughtExceptionHandler();

        // Assert
        assertThat(actual).isEqualTo(this.asyncUncaughtExceptionHandlerPublisher);
    }
}
