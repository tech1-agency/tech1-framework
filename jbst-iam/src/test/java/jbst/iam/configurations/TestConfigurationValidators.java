package jbst.iam.configurations;

import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.InvitationsRepository;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.repositories.UsersSessionsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import jbst.foundation.configurations.ConfigurationPropertiesJbstHardcoded;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;

import static org.mockito.Mockito.mock;

@Configuration
@Import({
        ConfigurationPropertiesJbstHardcoded.class
})
public class TestConfigurationValidators {

    // =================================================================================================================
    // Publishers
    // =================================================================================================================
    @Bean
    SecurityJwtPublisher securityJwtPublisher() {
        return mock(SecurityJwtPublisher.class);
    }

    @Bean
    SecurityJwtIncidentPublisher securityJwtIncidentPublisher() {
        return mock(SecurityJwtIncidentPublisher.class);
    }

    @Bean
    IncidentPublisher incidentPublisher() {
        return mock(IncidentPublisher.class);
    }

    // =================================================================================================================
    // Repositories
    // =================================================================================================================
    @Bean
    InvitationsRepository invitationsRepository() {
        return mock(InvitationsRepository.class);
    }

    @Bean
    UsersRepository userRepository() {
        return mock(UsersRepository.class);
    }

    @Bean
    UsersSessionsRepository userSessionRepository() {
        return mock(UsersSessionsRepository.class);
    }
}
