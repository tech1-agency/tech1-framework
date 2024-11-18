package tech1.framework.iam.events.subscribers.impl;

import tech1.framework.foundation.incidents.domain.authetication.*;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionExpired;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionRefreshed;
import tech1.framework.foundation.incidents.feigns.clients.IncidentClient;
import tech1.framework.iam.events.subscribers.SecurityJwtIncidentSubscriber;
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

import static tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SecurityJwtIncidentSubscriberImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentClient incidentClient() {
            return mock(IncidentClient.class);
        }

        @Bean
        SecurityJwtIncidentSubscriber securityJwtIncidentSubscriber() {
            return new SecurityJwtIncidentSubscriberImpl(
                    this.incidentClient()
            );
        }
    }

    // Clients
    private final IncidentClient incidentClient;

    private final SecurityJwtIncidentSubscriber componentUnderTest;

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
    void onEventAuthenticationLoginIncidentTest() {
        // Arrange
        var incident = entity(IncidentAuthenticationLogin.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventAuthenticationLoginFailureUsernamePasswordIncidentTest() {
        // Arrange
        var incident = entity(IncidentAuthenticationLoginFailureUsernamePassword.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventAuthenticationLoginFailureUsernameMaskedPasswordIncidentTest() {
        // Arrange
        var incident = entity(IncidentAuthenticationLoginFailureUsernameMaskedPassword.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventAuthenticationLogoutMinIncidentTest() {
        // Arrange
        var incident = entity(IncidentAuthenticationLogoutMin.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventAuthenticationLogoutFullIncidentTest() {
        // Arrange
        var incident = entity(IncidentAuthenticationLogoutFull.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventSessionRefreshedIncidentTest() {
        // Arrange
        var incident = entity(IncidentSessionRefreshed.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventSessionExpiredIncidentTest() {
        // Arrange
        var incident = entity(IncidentSessionExpired.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventRegister1IncidentTest() {
        // Arrange
        var incident = entity(IncidentRegistration1.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }

    @Test
    void onEventRegister1FailureIncidentTest() {
        // Arrange
        var incident = entity(IncidentRegistration1Failure.class);

        // Act
        this.componentUnderTest.onEvent(incident);

        // Assert
        verify(this.incidentClient).registerIncident(incident.getPlainIncident());
    }
}
