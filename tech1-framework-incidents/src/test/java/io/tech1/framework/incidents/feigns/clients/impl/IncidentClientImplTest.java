package io.tech1.framework.incidents.feigns.clients.impl;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientDefinition;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomFeignException;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentClientImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentClientDefinition incidentClientDefinition() {
            return mock(IncidentClientDefinition.class);
        }

        @Bean
        IncidentClient incidentPublisher() {
            return new IncidentClientImpl(
                    this.incidentClientDefinition()
            );
        }
    }

    // Definitions
    private final IncidentClientDefinition incidentClientDefinition;

    private final IncidentClient componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.incidentClientDefinition
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.incidentClientDefinition
        );
    }

    @Test
    public void registerIncidentExceptionTest() {
        // Arrange
        var incident = mock(Incident.class);
        doThrow(randomFeignException()).when(incidentClientDefinition).registerIncident(eq(incident));

        // Act
        this.componentUnderTest.registerIncident(incident);

        // Assert
        verify(this.incidentClientDefinition).registerIncident(eq(incident));
        // LOGGER ignored
    }

    @Test
    public void registerIncidentTest() {
        // Arrange
        var incident = mock(Incident.class);

        // Act
        this.componentUnderTest.registerIncident(incident);

        // Assert
        verify(this.incidentClientDefinition).registerIncident(eq(incident));
    }
}
