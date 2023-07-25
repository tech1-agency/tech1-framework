package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

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

import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_PORT;
import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_VERSION;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtDbRandomUtility.dummyInvitationCodesData1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                InvitationCodeRepository.class
        }
)
@Testcontainers
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class InvitationCodeRepositoryTest {

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
        this.invitationCodeRepository.deleteAll();
    }

    private final InvitationCodeRepository invitationCodeRepository;

    @Test
    void findByInvitedTests() {
        // Arrange
        this.invitationCodeRepository.saveAll(dummyInvitationCodesData1());

        // Act
        var used = this.invitationCodeRepository.findByInvitedAlreadyUsed();
        var notUsed = this.invitationCodeRepository.findByInvitedNotUsed();

        // Assert
        assertThat(used).hasSize(1);
        assertThat(notUsed).hasSize(5);
    }

    @Test
    void deleteByInvitedTests() {
        // Arrange
        this.invitationCodeRepository.saveAll(dummyInvitationCodesData1());

        // Act-1
        this.invitationCodeRepository.deleteByInvitedAlreadyUsed();

        // Assert-1
        assertThat(this.invitationCodeRepository.count()).isEqualTo(5);

        // Act-2
        this.invitationCodeRepository.deleteByInvitedNotUsed();

        // Assert-2
        assertThat(this.invitationCodeRepository.count()).isZero();
    }
}
