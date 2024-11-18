package jbst.foundation.configurations;

import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.domain.properties.configs.IncidentConfigs;
import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinition;
import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
            when(applicationFrameworkProperties.getIncidentConfigs()).thenReturn(IncidentConfigs.disabled());
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
