package jbst.foundation.incidents.events.publishers.impl;

import jbst.foundation.incidents.domain.Incident;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
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

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IncidentPublisherImplTest {

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
                    this.applicationEventPublisher()
            );
        }
    }

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final IncidentPublisher componentUnderTest;

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
    void publishResetServerStartedTest() {
        // Arrange
        var incident = IncidentSystemResetServerStarted.testsHardcoded();

        // Act
        this.componentUnderTest.publishResetServerStarted(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishResetServerCompletedTest() {
        // Arrange
        var incident = IncidentSystemResetServerCompleted.testsHardcoded();

        // Act
        this.componentUnderTest.publishResetServerCompleted(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishIncidentTest() {
        // Arrange
        var incident = Incident.random();

        // Act
        this.componentUnderTest.publishIncident(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(incident);
    }

    @Test
    void publishThrowableIncidentTest() {
        // Arrange
        var incident = new Incident(new Throwable("tech1"));

        // Act
        this.componentUnderTest.publishIncident(incident);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(incident);
    }
}
