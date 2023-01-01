package io.tech1.framework.incidents.events.subscribers.impl;

import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.events.subscribers.IncidentSubscriber;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
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

import static io.tech1.framework.incidents.tests.random.IncidentsRandomUtility.randomIncident;
import static io.tech1.framework.incidents.tests.random.IncidentsRandomUtility.randomThrowableIncident;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentSubscriberImplTest {

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
    public void beforeEach() {
        reset(
                this.incidentClient,
                this.incidentConverter
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.incidentClient,
                this.incidentConverter
        );
    }

    @Test
    public void onEventIncidentTest() {
        // Arrange
        var incident = randomIncident();

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventThrowableIncidentTest() {
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
