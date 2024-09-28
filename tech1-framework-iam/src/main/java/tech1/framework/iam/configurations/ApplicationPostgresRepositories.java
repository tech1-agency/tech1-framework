package tech1.framework.iam.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;
import tech1.framework.iam.repositories.postgres.Tech1PostgresRepositories;

@Configuration
@EntityScan({
        "tech1.framework.iam.domain.postgres"
})
@EnableJpaRepositories({
        "tech1.framework.iam.repositories.postgres"
})
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationPostgresRepositories {

    // Repositories
    private final PostgresInvitationCodesRepository invitationCodeRepository;
    private final PostgresUsersRepository userRepository;
    private final PostgresUsersSessionsRepository userSessionRepository;

    @Bean
    public Tech1PostgresRepositories tech1PostgresRepositories() {
        return new Tech1PostgresRepositories(
                this.invitationCodeRepository,
                this.userRepository,
                this.userSessionRepository
        );
    }
}
