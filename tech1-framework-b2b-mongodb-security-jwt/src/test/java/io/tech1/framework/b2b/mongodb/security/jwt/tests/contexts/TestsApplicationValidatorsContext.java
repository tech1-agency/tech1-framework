package io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts;

import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.b2b.mongodb.security.jwt.validators"
})
@Import({
        ApplicationFrameworkPropertiesContext.class
})
public class TestsApplicationValidatorsContext {

    // =================================================================================================================
    // Publishers
    // =================================================================================================================
    @Bean
    IncidentPublisher incidentPublisher() {
        return mock(IncidentPublisher.class);
    }

    // =================================================================================================================
    // Repositories
    // =================================================================================================================
    @Bean
    InvitationCodeRepository invitationCodeRepository() {
        return mock(InvitationCodeRepository.class);
    }

    @Bean
    UserRepository userRepository() {
        return mock(UserRepository.class);
    }
}
