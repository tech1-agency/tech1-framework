package io.tech1.framework.incidents.events.subscribers.impl;

import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.events.subscribers.IncidentSubscriber;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.incidents.tests.random.IncidentsRandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IncidentSubscriberImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentClient incidentClient() {
            return mock(IncidentClient.class);
        }

        @Bean
        IncidentConverter incidentConverter() {
            return mock(IncidentConverter.class);
        }

        @Bean
        IncidentSubscriber incidentPublisher() {
            return new IncidentSubscriberImpl(
                    this.incidentClient(),
                    this.incidentConverter()
            );
        }
    }

    // Clients
    private final IncidentClient incidentClient;
    // Converters
    private final IncidentConverter incidentConverter;

    private final IncidentSubscriber componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.incidentClient,
                this.incidentConverter
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.incidentClient,
                this.incidentConverter
        );
    }

    @Test
    void onEventIncidentSystemResetServerStartedTest() {
        // Arrange
        var incidentSystemResetServerStarted = randomIncidentSystemResetServerStarted();

        // Act
        this.componentUnderTest.onEvent(incidentSystemResetServerStarted);

        // Assert
        var incidentAC = ArgumentCaptor.forClass(Incident.class);
        verify(this.incidentClient).registerIncident(incidentAC.capture());
        var incident = incidentAC.getValue();
        assertThat(incident.getType()).isEqualTo("Reset Server Started");
        assertThat(incident.getUsername().identifier()).isEqualTo("tech1");
        assertThat(incident.getAttributes()).hasSize(2);
        assertThat(incident.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(incident.getAttributes().get("incidentType")).isEqualTo("Reset Server Started");
    }

    @Test
    void onEventIncidentSystemResetServerCompletedTest() {
        // Arrange
        var incidentSystemResetServerStarted = randomIncidentSystemResetServerCompleted();

        // Act
        this.componentUnderTest.onEvent(incidentSystemResetServerStarted);

        // Assert
        var incidentAC = ArgumentCaptor.forClass(Incident.class);
        verify(this.incidentClient).registerIncident(incidentAC.capture());
        var incident = incidentAC.getValue();
        assertThat(incident.getType()).isEqualTo("Reset Server Completed");
        assertThat(incident.getUsername().identifier()).isEqualTo("tech1");
        assertThat(incident.getAttributes()).hasSize(2);
        assertThat(incident.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(incident.getAttributes().get("incidentType")).isEqualTo("Reset Server Completed");
    }

    @Test
    void onEventIncidentTest() {
        // Arrange
        var incident = randomIncident();

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    void onEventThrowableIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var throwableIncident = randomThrowableIncident();
        when(this.incidentConverter.convert(eq(throwableIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(throwableIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(throwableIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }
}
