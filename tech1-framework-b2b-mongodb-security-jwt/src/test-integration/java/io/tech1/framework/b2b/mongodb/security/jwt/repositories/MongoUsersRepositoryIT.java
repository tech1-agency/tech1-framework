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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static io.tech1.framework.b2b.base.security.jwt.constants.SecurityJwtConstants.SUPERADMIN;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.converters.MongoUserConverter.toUsernamesAsStrings;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.MongoSecurityJwtDbDummies.dummyUsersData1;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                MongoUsersRepository.class
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoUsersRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final MongoUsersRepository usersRepository;

    @Override
    public MongoRepository<MongoDbUser, String> getMongoRepository() {
        return this.usersRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.usersRepository.saveAll(dummyUsersData1());

        // Act
        var superadmins = this.usersRepository.findByAuthoritySuperadmin();
        var notSuperadmins = this.usersRepository.findByAuthorityNotSuperadmin();
        var superadminsProjection = this.usersRepository.findByAuthorityProjectionUsernames(SUPERADMIN);
        var notSuperadminsProjection = this.usersRepository.findByAuthorityNotEqualProjectionUsernames(SUPERADMIN);
        var superadminsUsernames = this.usersRepository.findSuperadminsUsernames();
        var notSuperadminsUsernames = this.usersRepository.findNotSuperadminsUsernames();

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

        var jwtUser = this.usersRepository.loadUserByUsername(Username.of("sa1"));
        assertThat(jwtUser).isNotNull();
        assertThat(jwtUser.username()).isEqualTo(Username.of("sa1"));
        assertThat(jwtUser.password()).isNotNull();
        assertThat(jwtUser.authorities()).isNotNull();
        assertThat(jwtUser.isAccountNonExpired()).isTrue();
        assertThat(jwtUser.isAccountNonLocked()).isTrue();
        assertThat(jwtUser.isCredentialsNonExpired()).isTrue();
        assertThat(jwtUser.isEnabled()).isTrue();

        var username = randomUsername();
        var throwable = catchThrowable(() -> this.usersRepository.loadUserByUsername(username));
        assertThat(throwable)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Username: Not Found, id = " + username.identifier());
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.usersRepository.saveAll(dummyUsersData1());

        // Act-Assert-0
        assertThat(this.usersRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.usersRepository.deleteByAuthorityNotSuperadmin();
        var users1 = this.usersRepository.findAll();
        assertThat(toUsernamesAsStrings(users1))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");

        // Act-Assert-2
        this.usersRepository.deleteByAuthoritySuperadmin();
        var users2 = this.usersRepository.findAll();
        assertThat(users2).isEmpty();
    }
}
