package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
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
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbDummies.dummyUserSessionsData1;
import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbDummies.dummyUserSessionsData2;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;
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
        var sessions = this.usersSessionsRepository.findByUsernameIn(Set.of(Username.of("sa1"), Username.of("admin")));

        // Assert
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

    @Test
    void deleteByUsernameExceptAccessTokenTest() {
        // Arrange
        this.usersSessionsRepository.saveAll(dummyUserSessionsData2());

        // Act
        var count1 = this.usersSessionsRepository.count();
        this.usersSessionsRepository.deleteByUsernameExceptAccessToken(TECH1, new CookieAccessToken("token2"));
        var count2 = this.usersSessionsRepository.count();
        var sessions = this.usersSessionsRepository.findAll();

        assertThat(count1).isEqualTo(4);
        assertThat(count2).isEqualTo(2);
        var usernames = sessions.stream().map(PostgresDbUserSession::getUsername).collect(Collectors.toSet());
        var tokens = sessions.stream().map(session -> session.getAccessToken().value()).collect(Collectors.toSet());
        assertThat(usernames).isEqualTo(Set.of(TECH1, Username.of("admin")));
        assertThat(tokens).isEqualTo(Set.of("token2", "token4"));
    }

    @Test
    void deleteExceptAccessTokenTest() {
        // Arrange
        this.usersSessionsRepository.saveAll(dummyUserSessionsData2());

        // Act
        var count1 = this.usersSessionsRepository.count();
        this.usersSessionsRepository.deleteExceptAccessToken(new CookieAccessToken("token2"));
        var count2 = this.usersSessionsRepository.count();
        var sessions = this.usersSessionsRepository.findAll();

        assertThat(count1).isEqualTo(4);
        assertThat(count2).isEqualTo(1);
        var session = sessions.get(0);
        assertThat(session.getUsername()).isEqualTo(TECH1);
        assertThat(session.getAccessToken().value()).isEqualTo("token2");
    }
}
