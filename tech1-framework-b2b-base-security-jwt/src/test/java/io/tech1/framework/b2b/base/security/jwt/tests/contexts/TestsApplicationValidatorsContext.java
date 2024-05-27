package io.tech1.framework.b2b.base.security.jwt.tests.contexts;

import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
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
