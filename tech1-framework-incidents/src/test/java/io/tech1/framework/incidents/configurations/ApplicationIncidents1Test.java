package io.tech1.framework.incidents.configurations;

import io.tech1.framework.domain.tests.constants.TestsPropertiesConstants;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientDefinition;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientSlf4j;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
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
class ApplicationIncidents1Test {

    @Configuration
    @Import({
            ApplicationIncidents.class
    })
    static class ContextConfiguration {
        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            var applicationFrameworkProperties = mock(ApplicationFrameworkProperties.class);
            when(applicationFrameworkProperties.getAsyncConfigs()).thenReturn(TestsPropertiesConstants.ASYNC_CONFIGS);
            when(applicationFrameworkProperties.getEventsConfigs()).thenReturn(TestsPropertiesConstants.EVENTS_CONFIGS);
            when(applicationFrameworkProperties.getIncidentConfigs()).thenReturn(TestsPropertiesConstants.INCIDENT_CONFIGS);
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
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods).contains("incidentClientDefinition");
        assertThat(methods).hasSize(11);
    }

    @Test
    void incidentClientDefinitionTest() {
        // Arrange
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(TestsPropertiesConstants.INCIDENT_CONFIGS);

        // Act
        var incidentClientDefinition = this.componentUnderTest.incidentClientDefinition();

        // Assert
        assertThat(incidentClientDefinition.getClass()).isNotEqualTo(IncidentClientDefinition.class);
        assertThat(incidentClientDefinition.getClass()).isNotEqualTo(IncidentClientSlf4j.class);
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }
}
