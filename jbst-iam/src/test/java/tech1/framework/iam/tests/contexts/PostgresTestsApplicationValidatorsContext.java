package tech1.framework.iam.tests.contexts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "tech1.framework.iam.validators.postgres"
})
@Import({
        ApplicationFrameworkPropertiesTestsHardcodedContext.class
})
public class PostgresTestsApplicationValidatorsContext {

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
    PostgresInvitationCodesRepository invitationCodeRepository() {
        return mock(PostgresInvitationCodesRepository.class);
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
