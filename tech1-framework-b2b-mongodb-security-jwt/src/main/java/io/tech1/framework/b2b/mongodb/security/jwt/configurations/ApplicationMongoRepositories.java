package io.tech1.framework.b2b.mongodb.security.jwt.configurations;

import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.Tech1MongoRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationMongoRepositories {

    // Repositories
    private final MongoInvitationCodesRepository invitationCodeRepository;
    private final MongoUsersRepository userRepository;
    private final MongoUsersSessionsRepository userSessionRepository;

    @Bean
    public Tech1MongoRepositories tech1MongoRepositories() {
        return new Tech1MongoRepositories(
                this.invitationCodeRepository,
                this.userRepository,
                this.userSessionRepository
        );
    }
}
