package jbst.iam.configurations;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tech1.framework.foundation.domain.base.PropertyId;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.iam.assistants.userdetails.MongoUserDetailsAssistant;
import jbst.iam.essence.MongoBaseEssenceConstructor;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.mongodb.MongoInvitationCodesRepository;
import jbst.iam.repositories.mongodb.MongoUsersRepository;
import jbst.iam.repositories.mongodb.MongoUsersSessionsRepository;
import jbst.iam.services.mongodb.MongoBaseUsersSessionsService;
import jbst.iam.sessions.MongoSessionRegistry;

@Configuration
@ComponentScan({
        "tech1.framework.iam.services.mongodb",
        "tech1.framework.iam.validators.mongodb",
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
