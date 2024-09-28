package tech1.framework.iam.events.publishers.impl;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import tech1.framework.foundation.domain.properties.configs.SecurityJwtConfigs;
import tech1.framework.foundation.domain.properties.configs.security.jwt.IncidentsConfigs;
import tech1.framework.foundation.incidents.domain.authetication.*;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionExpired;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionRefreshed;
import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SecurityJwtIncidentPublisherImplTest {

    @Configuration
    static class ContextConfiguration {
        @Primary
        @Bean
        ApplicationEventPublisher applicationEventPublisher() {
            return mock(ApplicationEventPublisher.class);
        }

        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        SecurityJwtIncidentPublisher securityJwtIncidentPublisher() {
            return new SecurityJwtIncidentPublisherImpl(
                    this.applicationEventPublisher(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final SecurityJwtIncidentPublisher componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.applicationEventPublisher,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.applicationEventPublisher,
                this.applicationFrameworkProperties
        );
    }

    @Test
    void publishAuthenticationLoginDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGIN, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLogin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishAuthenticationLoginEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGIN, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLogin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishAuthenticationLoginFailureUsernamePasswordDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernamePassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernamePassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishAuthenticationLoginFailureUsernamePasswordEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernamePassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernamePassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishAuthenticationLoginFailureUsernameMaskedPasswordDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernameMaskedPassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernameMaskedPassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishAuthenticationLoginFailureUsernameMaskedPasswordEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernameMaskedPassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernameMaskedPassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishAuthenticationLogoutMinDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGOUT_MIN, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLogoutMin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutMin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishAuthenticationLogoutMinEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGOUT_MIN, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLogoutMin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutMin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishAuthenticationLogoutFullDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGOUT, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLogoutFull.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutFull(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishAuthenticationLogoutFullEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(AUTHENTICATION_LOGOUT, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentAuthenticationLogoutFull.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutFull(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishSessionRefreshedDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(SESSION_REFRESHED, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentSessionRefreshed.class);

        // Act
        this.componentUnderTest.publishSessionRefreshed(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishSessionRefreshedEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(SESSION_REFRESHED, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentSessionRefreshed.class);

        // Act
        this.componentUnderTest.publishSessionRefreshed(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishSessionExpiredDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(SESSION_EXPIRED, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentSessionExpired.class);

        // Act
        this.componentUnderTest.publishSessionExpired(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishSessionExpiredEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(SESSION_EXPIRED, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentSessionExpired.class);

        // Act
        this.componentUnderTest.publishSessionExpired(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishRegistration1DisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(REGISTER1, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentRegistration1.class);

        // Act
        this.componentUnderTest.publishRegistration1(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishRegistration1EnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(REGISTER1, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentRegistration1.class);

        // Act
        this.componentUnderTest.publishRegistration1(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishRegistration1FailureDisabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(REGISTER1_FAILURE, false);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentRegistration1Failure.class);

        // Act
        this.componentUnderTest.publishRegistration1Failure(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
    }

    @Test
    void publishRegistration1FailureEnabledTest() {
        // Arrange
        var securityJwtConfigs = randomSecurityJwtConfigs(REGISTER1_FAILURE, true);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(securityJwtConfigs);
        var incident = entity(IncidentRegistration1Failure.class);

        // Act
        this.componentUnderTest.publishRegistration1Failure(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private static SecurityJwtConfigs randomSecurityJwtConfigs(SecurityJwtIncidentType type, boolean enabled) {
        var typesConfigs = Stream.of(SecurityJwtIncidentType.values())
                .collect(Collectors.toMap(
                        entry -> entry,
                        entry -> type.equals(entry) && enabled
                ));
        return new SecurityJwtConfigs(
                null,
                null,
                null,
                new IncidentsConfigs(
                        typesConfigs
                ),
                null,
                null,
                null,
                null
        );
    }
}
