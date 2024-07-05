package io.tech1.framework.iam.configurations;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.iam.assistants.userdetails.MongoUserDetailsAssistant;
import io.tech1.framework.iam.essence.MongoBaseEssenceConstructor;
import io.tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.iam.repositories.mongodb.MongoInvitationCodesRepository;
import io.tech1.framework.iam.repositories.mongodb.MongoUsersRepository;
import io.tech1.framework.iam.repositories.mongodb.MongoUsersSessionsRepository;
import io.tech1.framework.iam.services.mongodb.MongoBaseUsersSessionsService;
import io.tech1.framework.iam.sessions.MongoSessionRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({
        "io.tech1.framework.iam.services.mongodb",
        "io.tech1.framework.iam.validators.mongodb",
})
@Import({
        ApplicationMongoRepositories.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationMongo {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getMongodbSecurityJwtConfigs().assertProperties(new PropertyId("mongodbSecurityJwtConfigs"));
    }

    @Bean
    MongoUserDetailsAssistant mongoUserDetailsAssistant(
            MongoUsersRepository mongoUsersRepository
    ) {
        return new MongoUserDetailsAssistant(
                mongoUsersRepository
        );
    }

    @Bean
    MongoBaseEssenceConstructor mongoBaseEssenceConstructor(
            MongoInvitationCodesRepository mongoInvitationCodesRepository,
            MongoUsersRepository mongoUsersRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        return new MongoBaseEssenceConstructor(
                mongoInvitationCodesRepository,
                mongoUsersRepository,
                applicationFrameworkProperties
        );
    }

    @Bean
    MongoSessionRegistry mongoSessionRegistry(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            MongoBaseUsersSessionsService mongoBaseUsersSessionsService,
            MongoUsersSessionsRepository mongoUsersSessionsRepository
    ) {
        return new MongoSessionRegistry(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                mongoBaseUsersSessionsService,
                mongoUsersSessionsRepository
        );
    }

}
