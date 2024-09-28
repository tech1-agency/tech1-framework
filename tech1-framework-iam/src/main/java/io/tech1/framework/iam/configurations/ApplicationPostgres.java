package io.tech1.framework.iam.configurations;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.iam.assistants.userdetails.PostgresUserDetailsAssistant;
import io.tech1.framework.iam.essence.PostgresBaseEssenceConstructor;
import io.tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import io.tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import io.tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;
import io.tech1.framework.iam.services.postgres.PostgresBaseUsersSessionsService;
import io.tech1.framework.iam.sessions.PostgresSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({
        "io.tech1.framework.iam.services.postgres",
        "io.tech1.framework.iam.validators.postgres",
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
