package jbst.iam.configurations;

import jbst.iam.assistants.userdetails.PostgresUserDetailsAssistant;
import jbst.iam.essence.PostgresBaseEssenceConstructor;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.postgres.PostgresInvitationsRepository;
import jbst.iam.repositories.postgres.PostgresUsersRepository;
import jbst.iam.repositories.postgres.PostgresUsersSessionsRepository;
import jbst.iam.services.postgres.PostgresBaseUsersSessionsService;
import jbst.iam.sessions.PostgresSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import jbst.foundation.domain.properties.JbstProperties;

@Configuration
@ComponentScan({
        "jbst.iam.services.postgres",
        "jbst.iam.validators.postgres",
})
@Import({
        ConfigurationPostgresRepositories.class
})
public class ConfigurationPostgres {

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
            PostgresInvitationsRepository postgresInvitationsRepository,
            PostgresUsersRepository postgresUsersRepository,
            JbstProperties jbstProperties
    ) {
        return new PostgresBaseEssenceConstructor(
                postgresInvitationsRepository,
                postgresUsersRepository,
                jbstProperties
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
