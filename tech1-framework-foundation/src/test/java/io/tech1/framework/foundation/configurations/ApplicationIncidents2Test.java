package io.tech1.framework.foundation.configurations;

import io.tech1.framework.foundation.domain.properties.configs.AsyncConfigs;
import io.tech1.framework.foundation.domain.properties.configs.EventsConfigs;
import io.tech1.framework.foundation.domain.properties.configs.IncidentConfigs;
import io.tech1.framework.foundation.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApplicationIncidents2Test {

    @Configuration
    @Import({
            ApplicationIncidents.class
    })
    static class ContextConfiguration {
        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            var applicationFrameworkProperties = mock(ApplicationFrameworkProperties.class);
            when(applicationFrameworkProperties.getAsyncConfigs()).thenReturn(AsyncConfigs.testsHardcoded());
            when(applicationFrameworkProperties.getEventsConfigs()).thenReturn(EventsConfigs.testsHardcoded());
            var incidentConfigs = IncidentConfigs.disabled();
            when(applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
            return applicationFrameworkProperties;
        }
    }

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final ApplicationIncidents componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.applicationFrameworkProperties
        );
    }

    @Test
    void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods)
                .contains("init")
                .contains("incidentClientDefinition")
                .contains("incidentClient")
                .hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void incidentClientDefinitionTest() {
        // Arrange
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(IncidentConfigs.disabled());

        // Act
        var incidentClientDefinition = this.componentUnderTest.incidentClientDefinition();

        // Assert
        assertThat(incidentClientDefinition.getClass()).isEqualTo(IncidentClientDefinitionSlf4j.class);
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }
}
