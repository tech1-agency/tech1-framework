package io.tech1.framework.incidents.events.publishers.impl;

import io.tech1.framework.domain.properties.base.RemoteServer;
import io.tech1.framework.domain.properties.configs.IncidentConfigs;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeaturesConfigs;
import io.tech1.framework.domain.utilities.random.EntityUtility;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
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
import java.util.stream.IntStream;

import static io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs.disabledIncidentFeatureConfigs;
import static io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs.enabledIncidentFeatureConfigs;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.incidents.tests.random.IncidentsRandomUtility.randomIncident;
import static io.tech1.framework.incidents.tests.random.IncidentsRandomUtility.randomThrowableIncident;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentPublisherImplTest {

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
        IncidentPublisher incidentPublisher() {
            return new IncidentPublisherImpl(
                    this.applicationEventPublisher(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final IncidentPublisher componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.applicationEventPublisher,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.applicationEventPublisher,
                this.applicationFrameworkProperties
        );
    }

    @Test
    public void publishIncidentTest() {
        // Arrange
        var incident = randomIncident();

        // Act
        this.componentUnderTest.publishIncident(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishThrowableIncidentTest() {
        // Arrange
        var incident = randomThrowableIncident();

        // Act
        this.componentUnderTest.publishThrowable(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishThrowableTest() {
        // Arrange
        var throwable = randomThrowableIncident().getThrowable();

        // Act
        this.componentUnderTest.publishThrowable(throwable);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(IncidentThrowable.of(throwable)));
    }

    @Test
    public void publishAuthenticationLoginDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(0, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLogin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishAuthenticationLoginEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(0, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLogin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishAuthenticationLoginFailureUsernamePasswordDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(1, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernamePassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernamePassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishAuthenticationLoginFailureUsernamePasswordEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(1, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernamePassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernamePassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishAuthenticationLoginFailureUsernameMaskedPasswordDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(2, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernameMaskedPassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernameMaskedPassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishAuthenticationLoginFailureUsernameMaskedPasswordEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(2, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLoginFailureUsernameMaskedPassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernameMaskedPassword(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishAuthenticationLogoutMinDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(3, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLogoutMin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutMin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishAuthenticationLogoutMinEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(3, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLogoutMin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutMin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishAuthenticationLogoutFullDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(3, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLogoutFull.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutFull(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishAuthenticationLogoutFullEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(3, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentAuthenticationLogoutFull.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogoutFull(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishSessionRefreshedDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(4, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentSessionRefreshed.class);

        // Act
        this.componentUnderTest.publishSessionRefreshed(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishSessionRefreshedEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(4, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentSessionRefreshed.class);

        // Act
        this.componentUnderTest.publishSessionRefreshed(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishSessionExpiredDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(5, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentSessionExpired.class);

        // Act
        this.componentUnderTest.publishSessionExpired(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishSessionExpiredEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(5, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentSessionExpired.class);

        // Act
        this.componentUnderTest.publishSessionExpired(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishRegistration1DisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(6, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentRegistration1.class);

        // Act
        this.componentUnderTest.publishRegistration1(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishRegistration1EnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(6, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentRegistration1.class);

        // Act
        this.componentUnderTest.publishRegistration1(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    @Test
    public void publishRegistration1FailureDisabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(7, false);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentRegistration1Failure.class);

        // Act
        this.componentUnderTest.publishRegistration1Failure(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishRegistration1FailureEnabledTest() {
        // Arrange
        var incidentConfigs = randomIncidentConfigsBy(7, true);
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(IncidentRegistration1Failure.class);

        // Act
        this.componentUnderTest.publishRegistration1Failure(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private static IncidentConfigs randomIncidentConfigsBy(int index, boolean enabled) {
        var incidentFeatureConfigs = IntStream.range(0, 8)
                .mapToObj(i -> {
                    if (index == i && enabled) {
                        return enabledIncidentFeatureConfigs();
                    } else {
                        return disabledIncidentFeatureConfigs();
                    }
                }).collect(Collectors.toList());
        return IncidentConfigs.of(
                true,
                EntityUtility.entity(RemoteServer.class),
                IncidentFeaturesConfigs.of(
                        incidentFeatureConfigs.get(0),
                        incidentFeatureConfigs.get(1),
                        incidentFeatureConfigs.get(2),
                        incidentFeatureConfigs.get(3),
                        incidentFeatureConfigs.get(4),
                        incidentFeatureConfigs.get(5),
                        incidentFeatureConfigs.get(6),
                        incidentFeatureConfigs.get(7)
                )
        );
    }
}
