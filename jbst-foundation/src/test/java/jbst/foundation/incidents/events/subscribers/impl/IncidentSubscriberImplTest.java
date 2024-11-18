package jbst.foundation.incidents.events.subscribers.impl;

import jbst.foundation.incidents.domain.Incident;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import jbst.foundation.incidents.events.subscribers.IncidentSubscriber;
import jbst.foundation.incidents.feigns.clients.IncidentClient;
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
        IncidentSubscriber incidentPublisher() {
            return new IncidentSubscriberImpl(
                    this.incidentClient()
            );
        }
    }

    // Clients
    private final IncidentClient incidentClient;

    private final IncidentSubscriber componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.incidentClient
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.incidentClient
        );
    }

    @Test
    void onEventIncidentSystemResetServerStartedTest() {
        // Arrange
        var incidentSystemResetServerStarted = IncidentSystemResetServerStarted.testsHardcoded();

        // Act
        this.componentUnderTest.onEvent(incidentSystemResetServerStarted);

        // Assert
        var incidentAC = ArgumentCaptor.forClass(Incident.class);
        verify(this.incidentClient).registerIncident(incidentAC.capture());
        var incident = incidentAC.getValue();
        assertThat(incident.getType()).isEqualTo("Reset Server Started");
        assertThat(incident.getUsername().value()).isEqualTo("jbst");
        assertThat(incident.getAttributes()).hasSize(2);
        assertThat(incident.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(incident.getAttributes()).containsEntry("incidentType", "Reset Server Started");
    }

    @Test
    void onEventIncidentSystemResetServerCompletedTest() {
        // Arrange
        var incidentSystemResetServerStarted = IncidentSystemResetServerCompleted.testsHardcoded();

        // Act
        this.componentUnderTest.onEvent(incidentSystemResetServerStarted);

        // Assert
        var incidentAC = ArgumentCaptor.forClass(Incident.class);
        verify(this.incidentClient).registerIncident(incidentAC.capture());
        var incident = incidentAC.getValue();
        assertThat(incident.getType()).isEqualTo("Reset Server Completed");
        assertThat(incident.getUsername().value()).isEqualTo("jbst");
        assertThat(incident.getAttributes()).hasSize(2);
        assertThat(incident.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(incident.getAttributes()).containsEntry("incidentType", "Reset Server Completed");
    }

    @Test
    void onEventIncidentTest() {
        // Arrange
        var incident = Incident.random();

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident);
    }
}
