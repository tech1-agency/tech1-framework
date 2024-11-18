package jbst.foundation.configurations;

import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.domain.properties.configs.IncidentConfigs;
import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinition;
import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
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
        properties = "jbst.incident-configs.enabled=true"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ConfigurationIncidents1Test {

    @Configuration
    @Import({
            ConfigurationIncidents.class
    })
    static class ContextConfiguration {
        @Bean
        JbstProperties applicationFrameworkProperties() {
            var applicationFrameworkProperties = mock(JbstProperties.class);
            when(applicationFrameworkProperties.getIncidentConfigs()).thenReturn(IncidentConfigs.hardcoded());
            return applicationFrameworkProperties;
        }
    }

    // Properties
    private final JbstProperties jbstProperties;

    private final ConfigurationIncidents componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.jbstProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.jbstProperties
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
                .hasSizeGreaterThanOrEqualTo(24);
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
