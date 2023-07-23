package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_PORT;
import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_VERSION;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtDbRandomUtility.dummyUserSessionsData1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                UserSessionRepository.class
        }
)
@Testcontainers
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserSessionRepositoryTest {

    @Container
    private static final MongoDBContainer container = new MongoDBContainer(MONGO_DB_VERSION).withExposedPorts(MONGO_DB_PORT);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

    @BeforeAll
    public static void beforeAll() {
        container.start();
    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    @AfterEach
    void afterEach() {
        this.userSessionRepository.deleteAll();
    }

    private final UserSessionRepository userSessionRepository;

    @Test
    void findByUsernamesTest() {
        // Arrange
        this.userSessionRepository.saveAll(dummyUserSessionsData1());

        // Act
        var sessions = this.userSessionRepository.findByUsernames(List.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(sessions).isNotNull();
        assertThat(sessions).hasSize(5);
    }

    @Test
    void deleteByUsernamesTest() {
        // Arrange
        this.userSessionRepository.saveAll(dummyUserSessionsData1());

        // Act
        this.userSessionRepository.deleteByUsernames(List.of(Username.of("sa1"), Username.of("admin")));

        // Assert
        assertThat(this.userSessionRepository.count()).isEqualTo(2);
    }
}
