package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
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

import static io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserRepository.SUPERADMIN;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.converters.UserConverter.toUsernamesAsStrings;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtDbRandomUtility.dummyUsersData1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                MongoUserRepository.class
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoUserRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final MongoUserRepository mongoUserRepository;

    @Override
    public MongoRepository<MongoDbUser, String> getMongoRepository() {
        return this.mongoUserRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.mongoUserRepository.saveAll(dummyUsersData1());

        // Act
        var superadmins = this.mongoUserRepository.findByAuthoritySuperadmin();
        var notSuperadmins = this.mongoUserRepository.findByAuthorityNotSuperadmin();
        var superadminsProjection = this.mongoUserRepository.findByAuthorityProjectionUsernames(SUPERADMIN);
        var notSuperadminsProjection = this.mongoUserRepository.findByAuthorityNotEqualProjectionUsernames(SUPERADMIN);
        var superadminsUsernames = this.mongoUserRepository.findSuperadminsUsernames();
        var notSuperadminsUsernames = this.mongoUserRepository.findNotSuperadminsUsernames();

        // Assert
        assertThat(toUsernamesAsStrings(superadmins))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");

        assertThat(toUsernamesAsStrings(notSuperadmins))
                .hasSize(3)
                .containsExactly("admin", "user1", "user2");

        assertThat(toUsernamesAsStrings(superadminsProjection))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");
        superadminsProjection.forEach(user -> {
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getPassword()).isNull();
            assertThat(user.getAuthorities()).isNull();
            assertThat(user.getZoneId()).isNull();
        });

        assertThat(toUsernamesAsStrings(notSuperadminsProjection))
                .hasSize(3)
                .containsExactly("admin", "user1", "user2");
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
    void deletionIntegrationTests() {
        // Arrange
        this.mongoUserRepository.saveAll(dummyUsersData1());

        // Act-Assert-0
        assertThat(this.mongoUserRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.mongoUserRepository.deleteByAuthorityNotSuperadmin();
        var users1 = this.mongoUserRepository.findAll();
        assertThat(toUsernamesAsStrings(users1))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");

        // Act-Assert-2
        this.mongoUserRepository.deleteByAuthoritySuperadmin();
        var users2 = this.mongoUserRepository.findAll();
        assertThat(users2).isEmpty();
    }
}
