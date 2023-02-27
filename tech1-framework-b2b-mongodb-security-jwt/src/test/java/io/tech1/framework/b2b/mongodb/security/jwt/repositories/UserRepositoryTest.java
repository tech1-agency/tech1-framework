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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
        var superadminAuthority = new SimpleGrantedAuthority(SUPER_ADMIN);
        this.userRepository.saveAll(dummyUsersData1());

        // Act
        var superadmins = this.userRepository.findByAuthoritySuperadmin();
        var notSuperadmins = this.userRepository.findByAuthorityNotSuperadmin();
        var superadminsProjection = this.userRepository.findByAuthorityProjectionUsernames(superadminAuthority);
        var notSuperadminsProjection = this.userRepository.findByAuthorityNotEqualProjectionUsernames(superadminAuthority);

        // Assert
        assertThat(superadmins).isNotNull();
        assertThat(superadmins).hasSize(3);
        assertThat(toUsernamesAsStrings(superadmins)).containsExactly("sa1", "sa2", "sa3");

        assertThat(notSuperadmins).isNotNull();
        assertThat(notSuperadmins).hasSize(3);
        assertThat(toUsernamesAsStrings(notSuperadmins)).containsExactly("admin", "user1", "user2");

        assertThat(superadminsProjection).isNotNull();
        assertThat(superadminsProjection).hasSize(3);
        assertThat(toUsernamesAsStrings(superadminsProjection)).containsExactly("sa1", "sa2", "sa3");
        superadminsProjection.forEach(user -> {
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getPassword()).isNull();
            assertThat(user.getAuthorities()).isNull();
            assertThat(user.getZoneId()).isNull();
        });

        assertThat(notSuperadminsProjection).isNotNull();
        assertThat(notSuperadminsProjection).hasSize(3);
        assertThat(toUsernamesAsStrings(notSuperadminsProjection)).containsExactly("admin", "user1", "user2");
        notSuperadminsProjection.forEach(user -> {
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getPassword()).isNull();
            assertThat(user.getAuthorities()).isNull();
            assertThat(user.getZoneId()).isNull();
        });
    }
}
