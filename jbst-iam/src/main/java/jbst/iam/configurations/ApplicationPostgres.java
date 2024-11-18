package jbst.iam.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.iam.assistants.userdetails.PostgresUserDetailsAssistant;
import jbst.iam.essence.PostgresBaseEssenceConstructor;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.postgres.PostgresInvitationCodesRepository;
import jbst.iam.repositories.postgres.PostgresUsersRepository;
import jbst.iam.repositories.postgres.PostgresUsersSessionsRepository;
import jbst.iam.services.postgres.PostgresBaseUsersSessionsService;
import jbst.iam.sessions.PostgresSessionRegistry;

@Configuration
@ComponentScan({
        "tech1.framework.iam.services.postgres",
        "tech1.framework.iam.validators.postgres",
})
@Import({
        ApplicationPostgresRepositories.class
})
public class ApplicationPostgres {

    @Bean
    PostgresUserDetailsAssistant postgresUserDetailsAssistant(
            PostgresUsersRepository postgresUsersRepository
    ) {
        return new PostgresUserDetailsAssistant(
                postgresUsersRepository
        );
    }

    @Bean
    PostgresBaseEssenceConstructor postgresBaseEssenceConstructor(
            PostgresInvitationCodesRepository postgresInvitationCodesRepository,
            PostgresUsersRepository postgresUsersRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        return new PostgresBaseEssenceConstructor(
                postgresInvitationCodesRepository,
                postgresUsersRepository,
                applicationFrameworkProperties
        );
    }

    @Bean
    PostgresSessionRegistry postgresSessionRegistry(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            PostgresBaseUsersSessionsService postgresBaseUsersSessionsService,
            PostgresUsersSessionsRepository postgresUsersSessionsRepository
    ) {
        return new PostgresSessionRegistry(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                postgresBaseUsersSessionsService,
                postgresUsersSessionsRepository
        );
    }

}
