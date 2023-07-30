package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbDummies.dummyUserSessionsData1;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                PostgresUsersSessionsRepository.class
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresUsersSessionsRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final PostgresUsersSessionsRepository usersSessionsRepository;

    @Override
    public JpaRepository<PostgresDbUserSession, String> getJpaRepository() {
        return this.usersSessionsRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.usersSessionsRepository.saveAll(dummyUserSessionsData1());

        // Act
        var count = this.usersSessionsRepository.count();
        var sessions = this.usersSessionsRepository.findByUsernameIn(Set.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(count).isEqualTo(7);
        assertThat(sessions).hasSize(5);
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.usersSessionsRepository.saveAll(dummyUserSessionsData1());

        // Act
        this.usersSessionsRepository.deleteByUsernames(Set.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(this.usersSessionsRepository.count()).isEqualTo(2);
    }
}
