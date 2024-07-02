package io.tech1.framework.foundation.configurations;

import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.domain.properties.configs.IncidentConfigs;
import io.tech1.framework.foundation.incidents.feigns.definitions.IncidentClientDefinition;
import io.tech1.framework.foundation.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "tech1.incident-configs.enabled=true"
)
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
            when(applicationFrameworkProperties.getIncidentConfigs()).thenReturn(IncidentConfigs.testsHardcoded());
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
                .contains("asyncUncaughtExceptionHandler")
                .contains("incidentClientDefinition")
                .contains("incidentClient")
                .contains("incidentPublisher")
                .contains("incidentSubscriber")
                .contains("errorHandler")
                .contains("rejectedExecutionHandler")
                .hasSize(24);
    }

    @Test
    void incidentClientDefinitionTest() {
        // Act
        var incidentClientDefinition = this.componentUnderTest.incidentClientDefinition();

        // Assert
        assertThat(incidentClientDefinition.getClass()).isNotEqualTo(IncidentClientDefinition.class);
        assertThat(incidentClientDefinition.getClass()).isNotEqualTo(IncidentClientDefinitionSlf4j.class);
    }

    @Test
    void incidentClientDefinitionSlf4jTest() {
        // Act + Assert
        assertThatThrownBy(this.componentUnderTest::incidentClientDefinitionSlf4j)
                .isInstanceOf(NoSuchBeanDefinitionException.class)
                .hasMessage("No bean named 'incidentClientDefinitionSlf4j' available");
    }
}
