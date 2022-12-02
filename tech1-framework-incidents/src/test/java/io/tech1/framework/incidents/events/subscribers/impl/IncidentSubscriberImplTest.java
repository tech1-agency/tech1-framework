package io.tech1.framework.incidents.events.subscribers.impl;

import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.Registration1FailureIncident;
import io.tech1.framework.incidents.domain.registration.Registration1Incident;
import io.tech1.framework.incidents.domain.session.SessionExpiredIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
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

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
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

    @Test
    public void onEventAuthenticationLoginIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var authenticationLoginIncident = entity(AuthenticationLoginIncident.class);
        when(this.incidentConverter.convert(eq(authenticationLoginIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(authenticationLoginIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(authenticationLoginIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventAuthenticationLoginFailureUsernamePasswordIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var authenticationLoginFailureUsernamePasswordIncident = entity(AuthenticationLoginFailureUsernamePasswordIncident.class);
        when(this.incidentConverter.convert(eq(authenticationLoginFailureUsernamePasswordIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(authenticationLoginFailureUsernamePasswordIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(authenticationLoginFailureUsernamePasswordIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventAuthenticationLoginFailureUsernameMaskedPasswordIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var authenticationLoginFailureUsernameMaskedPasswordIncident = entity(AuthenticationLoginFailureUsernameMaskedPasswordIncident.class);
        when(this.incidentConverter.convert(eq(authenticationLoginFailureUsernameMaskedPasswordIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(authenticationLoginFailureUsernameMaskedPasswordIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(authenticationLoginFailureUsernameMaskedPasswordIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventAuthenticationLogoutMinIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var authenticationLogoutMinIncident = entity(AuthenticationLogoutMinIncident.class);
        when(this.incidentConverter.convert(eq(authenticationLogoutMinIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(authenticationLogoutMinIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(authenticationLogoutMinIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventAuthenticationLogoutFullIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var authenticationLogoutFullIncident = entity(AuthenticationLogoutFullIncident.class);
        when(this.incidentConverter.convert(eq(authenticationLogoutFullIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(authenticationLogoutFullIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(authenticationLogoutFullIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventSessionRefreshedIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var sessionRefreshedIncident = entity(SessionRefreshedIncident.class);
        when(this.incidentConverter.convert(eq(sessionRefreshedIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(sessionRefreshedIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(sessionRefreshedIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventSessionExpiredIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var sessionExpiredIncident = entity(SessionExpiredIncident.class);
        when(this.incidentConverter.convert(eq(sessionExpiredIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(sessionExpiredIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(sessionExpiredIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventRegister1IncidentTest() {
        // Arrange
        var incident = randomIncident();
        var register1Incident = entity(Registration1Incident.class);
        when(this.incidentConverter.convert(eq(register1Incident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(register1Incident);

        // Assert
        verify(this.incidentConverter).convert(eq(register1Incident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }

    @Test
    public void onEventRegister1FailureIncidentTest() {
        // Arrange
        var incident = randomIncident();
        var register1FailureIncident = entity(Registration1FailureIncident.class);
        when(this.incidentConverter.convert(eq(register1FailureIncident))).thenReturn(incident);

        // Act
        this.componentUnderTest.onEvent(register1FailureIncident);

        // Assert
        verify(this.incidentConverter).convert(eq(register1FailureIncident));
        verify(this.incidentClient).registerIncident(eq(incident));
    }
}
