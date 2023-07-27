package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtDbRandomUtility.dummyUserSessionsData1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                UserSessionRepository.class
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserSessionRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final UserSessionRepository userSessionRepository;

    @Override
    public MongoRepository<DbUserSession, String> getMongoRepository() {
        return this.userSessionRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.userSessionRepository.saveAll(dummyUserSessionsData1());

        // Act
        var sessions = this.userSessionRepository.findByUsernames(List.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(sessions).hasSize(5);
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.userSessionRepository.saveAll(dummyUserSessionsData1());

        // Act
        this.userSessionRepository.deleteByUsernames(List.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(this.userSessionRepository.count()).isEqualTo(2);
    }
}
