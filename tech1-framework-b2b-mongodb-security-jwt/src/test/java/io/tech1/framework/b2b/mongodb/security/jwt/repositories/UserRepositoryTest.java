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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_PORT;
import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.RepositoriesConstants.MONGO_DB_VERSION;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.converters.UserConverter.toUsernamesAsStrings;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtDbRandomUtility.dummyUsersData1;
import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
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
class UserRepositoryTest {

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
        this.userRepository.deleteAll();
    }

    private final UserRepository userRepository;

    @Test
    void findByAuthoritiesTests() {
        // Arrange
        var superadminAuthority = new SimpleGrantedAuthority(SUPER_ADMIN);
        this.userRepository.saveAll(dummyUsersData1());

        // Act
        var superadmins = this.userRepository.findByAuthoritySuperadmin();
        var notSuperadmins = this.userRepository.findByAuthorityNotSuperadmin();
        var superadminsProjection = this.userRepository.findByAuthorityProjectionUsernames(superadminAuthority);
        var notSuperadminsProjection = this.userRepository.findByAuthorityNotEqualProjectionUsernames(superadminAuthority);
        var superadminsUsernames = this.userRepository.findSuperadminsUsernames();
        var notSuperadminsUsernames = this.userRepository.findNotSuperadminsUsernames();

        // Assert
        assertThat(superadmins).hasSize(3);
        assertThat(toUsernamesAsStrings(superadmins)).containsExactly("sa1", "sa2", "sa3");

        assertThat(notSuperadmins).hasSize(3);
        assertThat(toUsernamesAsStrings(notSuperadmins)).containsExactly("admin", "user1", "user2");

        assertThat(superadminsProjection).hasSize(3);
        assertThat(toUsernamesAsStrings(superadminsProjection)).containsExactly("sa1", "sa2", "sa3");
        superadminsProjection.forEach(user -> {
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getPassword()).isNull();
            assertThat(user.getAuthorities()).isNull();
            assertThat(user.getZoneId()).isNull();
        });

        assertThat(notSuperadminsProjection).hasSize(3);
        assertThat(toUsernamesAsStrings(notSuperadminsProjection)).containsExactly("admin", "user1", "user2");
        notSuperadminsProjection.forEach(user -> {
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getPassword()).isNull();
            assertThat(user.getAuthorities()).isNull();
            assertThat(user.getZoneId()).isNull();
        });

        assertThat(superadminsUsernames)
                .hasSize(3)
                .isEqualTo(
                    List.of(
                            Username.of("sa1"),
                            Username.of("sa2"),
                            Username.of("sa3")
                    )
                );

        assertThat(notSuperadminsUsernames)
                .hasSize(3)
                .isEqualTo(
                    List.of(
                            Username.of("admin"),
                            Username.of("user1"),
                            Username.of("user2")
                    )
                );
    }

    @Test
    void deleteByAuthoritiesTests() {
        // Arrange
        this.userRepository.saveAll(dummyUsersData1());

        // Act-1
        this.userRepository.deleteByAuthorityNotSuperadmin();

        // Assert-1
        var users1 = this.userRepository.findAll();
        assertThat(users1).hasSize(3);
        assertThat(toUsernamesAsStrings(users1)).containsExactly("sa1", "sa2", "sa3");

        // Act-2
        this.userRepository.deleteByAuthoritySuperadmin();

        // Assert-2
        var users2 = this.userRepository.findAll();
        assertThat(users2).isEmpty();
    }
}
