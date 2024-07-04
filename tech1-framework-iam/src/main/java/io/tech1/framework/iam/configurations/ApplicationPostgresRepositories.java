package io.tech1.framework.iam.configurations;

import io.tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import io.tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import io.tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;
import io.tech1.framework.iam.repositories.postgres.Tech1PostgresRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan({
        "io.tech1.framework.iam.domain.postgres"
})
@EnableJpaRepositories({
        "io.tech1.framework.iam.repositories.postgres"
})
@EnableTransactionManagement
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
