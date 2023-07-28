package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
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
                MongoUserSessionRepository.class
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoUserSessionRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final MongoUserSessionRepository mongoUserSessionRepository;

    @Override
    public MongoRepository<MongoDbUserSession, String> getMongoRepository() {
        return this.mongoUserSessionRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.mongoUserSessionRepository.saveAll(dummyUserSessionsData1());

        // Act
        var sessions = this.mongoUserSessionRepository.findByUsernames(List.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(sessions).hasSize(5);
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.mongoUserSessionRepository.saveAll(dummyUserSessionsData1());

        // Act
        this.mongoUserSessionRepository.deleteByUsernames(List.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(this.mongoUserSessionRepository.count()).isEqualTo(2);
    }
}
