package tech1.framework.iam.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.iam.assistants.userdetails.PostgresUserDetailsAssistant;
import tech1.framework.iam.essence.PostgresBaseEssenceConstructor;
import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;
import tech1.framework.iam.services.postgres.PostgresBaseUsersSessionsService;
import tech1.framework.iam.sessions.PostgresSessionRegistry;

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
