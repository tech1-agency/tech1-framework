package io.tech1.framework.b2b.postgres.security.jwt.configurations;

import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.Tech1PostgresRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
