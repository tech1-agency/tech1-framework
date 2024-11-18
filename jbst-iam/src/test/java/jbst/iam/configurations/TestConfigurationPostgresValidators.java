package jbst.iam.configurations;

import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.postgres.PostgresInvitationsRepository;
import jbst.iam.repositories.postgres.PostgresUsersRepository;
import jbst.iam.repositories.postgres.PostgresUsersSessionsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import jbst.foundation.configurations.ConfigurationPropertiesJbstHardcoded;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "jbst.iam.validators.postgres"
})
@Import({
        ConfigurationPropertiesJbstHardcoded.class
})
public class TestConfigurationPostgresValidators {

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
    PostgresInvitationsRepository invitationsRepository() {
        return mock(PostgresInvitationsRepository.class);
    }

    @Bean
    PostgresUsersRepository userRepository() {
        return mock(PostgresUsersRepository.class);
    }

    @Bean
    PostgresUsersSessionsRepository userSessionRepository() {
        return mock(PostgresUsersSessionsRepository.class);
    }
}
