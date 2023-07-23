package io.tech1.framework.incidents.feigns.definitions;

import io.tech1.framework.incidents.domain.Incident;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.mockito.Mockito.mock;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IncidentClientSlf4jTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentClientDefinition incidentClientDefinition() {
            return new IncidentClientSlf4j();
        }
    }

    private final IncidentClientDefinition componentUnderTest;

    @Test
    void registerIncidentExceptionTest() {
        // Arrange
        var incident = mock(Incident.class);

        // Act
        this.componentUnderTest.registerIncident(incident);

        // Assert
        // LOGGER ignored
    }
}
