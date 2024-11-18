package tech1.framework.foundation.incidents.feigns.definitions;

import tech1.framework.foundation.incidents.domain.Incident;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IncidentClientDefinitionSlf4JTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentClientDefinition incidentClientDefinition() {
            return new IncidentClientDefinitionSlf4j();
        }
    }

    private final IncidentClientDefinition componentUnderTest;

    @Test
    void registerIncidentExceptionTest() {
        // Arrange
        var incident = mock(Incident.class);

        // Act
        this.componentUnderTest.registerIncident(incident);

        // Assert
        verify(incident).print();
        verifyNoMoreInteractions(incident);
    }
}
