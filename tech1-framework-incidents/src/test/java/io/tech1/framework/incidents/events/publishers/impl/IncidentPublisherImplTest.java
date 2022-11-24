package io.tech1.framework.incidents.events.publishers.impl;

import io.tech1.framework.domain.properties.base.RemoteServer;
import io.tech1.framework.domain.properties.configs.IncidentConfigs;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeaturesConfigs;
import io.tech1.framework.domain.utilities.random.EntityUtility;
import io.tech1.framework.incidents.domain.authetication.AuthenticationLoginIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
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
        verify(this.applicationEventPublisher).publishEvent(eq(ThrowableIncident.of(throwable)));
    }

    @Test
    public void publishAuthenticationLoginDisabledTest() {
        // Arrange
        var incidentConfigs = IncidentConfigs.of(
                true,
                EntityUtility.entity(RemoteServer.class),
                IncidentFeaturesConfigs.of(
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs()
                )
        );
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(AuthenticationLoginIncident.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
    }

    @Test
    public void publishAuthenticationLoginEnabledTest() {
        // Arrange
        var incidentConfigs = IncidentConfigs.of(
                true,
                EntityUtility.entity(RemoteServer.class),
                IncidentFeaturesConfigs.of(
                        enabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs()
                )
        );
        when(this.applicationFrameworkProperties.getIncidentConfigs()).thenReturn(incidentConfigs);
        var incident = entity(AuthenticationLoginIncident.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogin(incident);

        // Assert
        verify(this.applicationFrameworkProperties).getIncidentConfigs();
        verify(this.applicationEventPublisher).publishEvent(eq(incident));
    }
}
