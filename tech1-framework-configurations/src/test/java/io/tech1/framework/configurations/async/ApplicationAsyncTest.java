package io.tech1.framework.configurations.async;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.processors.ProcessorsUtility.getHalfOfCores;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApplicationAsyncTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class,
            ApplicationAsync.class
    })
    static class ContextConfiguration {

    }

    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final ApplicationAsync componentUnderTest;

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
        // Act
        var actual = this.componentUnderTest.getAsyncExecutor();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getClass()).isEqualTo(ThreadPoolTaskExecutor.class);
        var threadPoolTaskExecutor = (ThreadPoolTaskExecutor) actual;
        assertThat(threadPoolTaskExecutor.getCorePoolSize()).isEqualTo(getHalfOfCores());
        assertThat(threadPoolTaskExecutor.getThreadNamePrefix()).isEqualTo(this.applicationFrameworkProperties.getAsyncConfigs().getThreadNamePrefix());
    }

    @Test
    void getAsyncUncaughtExceptionHandlerTest() {
        // Act
        var actual = this.componentUnderTest.getAsyncUncaughtExceptionHandler();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getClass()).isEqualTo(SimpleAsyncUncaughtExceptionHandler.class);
    }
}
