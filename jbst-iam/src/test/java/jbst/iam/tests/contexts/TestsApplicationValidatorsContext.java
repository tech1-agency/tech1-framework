package jbst.iam.tests.contexts;

import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.InvitationCodesRepository;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.repositories.UsersSessionsRepository;
import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Configuration
@Import({
        ApplicationFrameworkPropertiesTestsHardcodedContext.class
})
public class TestsApplicationValidatorsContext {

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
    InvitationCodesRepository invitationCodeRepository() {
        return mock(InvitationCodesRepository.class);
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
