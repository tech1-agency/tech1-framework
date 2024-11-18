package jbst.foundation.configurations;

import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinition;
import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "jbst.incident-configs.enabled=false"
        }
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ConfigurationIncidents2Test {

    @Configuration
    @Import({
            ConfigurationIncidents.class
    })
    static class ContextConfiguration {

    }

    private final ConfigurationIncidents componentUnderTest;

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
        // Act + Assert
        assertThatThrownBy(this.componentUnderTest::incidentClientDefinition)
                .isInstanceOf(NoSuchBeanDefinitionException.class)
                .hasMessage("No bean named 'incidentClientDefinition' available");
    }

    @Test
    void incidentClientDefinitionSlf4jTest() {
        // Act
        var incidentClientDefinition = this.componentUnderTest.incidentClientDefinitionSlf4j();

        // Assert
        assertThat(incidentClientDefinition.getClass()).isNotEqualTo(IncidentClientDefinition.class);
        assertThat(incidentClientDefinition.getClass()).isEqualTo(IncidentClientDefinitionSlf4j.class);
    }
}
