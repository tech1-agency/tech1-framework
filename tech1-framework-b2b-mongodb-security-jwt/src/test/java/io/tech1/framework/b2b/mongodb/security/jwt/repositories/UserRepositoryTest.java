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

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_PORT;
import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_VERSION;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtDbRandomUtility.*;
import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                UserRepository.class
        }
)
@Testcontainers
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryTest {

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
    public void afterEach() {
        this.userRepository.deleteAll();
    }

    private final UserRepository userRepository;

    @Test
    public void findSuperadminsTest() {
        // Arrange
        var users = List.of(
                superadmin("sa1"),
                superadmin("sa2"),
                admin("admin"),
                randomUserBy("user1", List.of("user", INVITATION_CODE_WRITE)),
                randomUserBy("user2", List.of("user", INVITATION_CODE_READ)),
                randomUserBy("sa3", List.of(INVITATION_CODE_READ, SUPER_ADMIN, INVITATION_CODE_WRITE))
        );
        this.userRepository.saveAll(users);

        // Act
        var superadmins = this.userRepository.findSuperadmins();

        // Assert
        var usernames = superadmins.stream()
                .map(superadmin -> superadmin.getUsername().toString())
                .collect(Collectors.toList());

        assertThat(superadmins).isNotNull();
        assertThat(superadmins).hasSize(3);
        assertThat(usernames).containsExactly("sa1", "sa2", "sa3");
    }
}
