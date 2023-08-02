package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.converters.UserConverter.toUsernamesAsStrings0;
import static io.tech1.framework.b2b.postgres.security.jwt.tests.converters.UserConverter.toUsernamesAsStrings1;
import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbDummies.dummyUsersData1;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                PostgresUsersRepository.class,
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresUsersRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final PostgresUsersRepository usersRepository;

    @Override
    public JpaRepository<PostgresDbUser, String> getJpaRepository() {
        return this.usersRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.usersRepository.saveAll(dummyUsersData1());

        // Act
        var count = this.usersRepository.count();
        var superadmins = this.usersRepository.findByAuthoritySuperadmin();
        var notSuperadmins = this.usersRepository.findByAuthorityNotSuperadmin();
        var superadminsUsernames = this.usersRepository.findSuperadminsUsernames();
        var notSuperadminsUsernames = this.usersRepository.findNotSuperadminsUsernames();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.usersRepository.findByEmail(Email.of("sa1@tech1.io"))).isNotNull();
        assertThat(this.usersRepository.findByEmail(Email.of("sa2@tech1.io"))).isNotNull();
        assertThat(this.usersRepository.findByEmail(Email.of("sa4@tech1.io"))).isNull();
        assertThat(this.usersRepository.findByUsername(Username.of("sa1"))).isNotNull();
        assertThat(this.usersRepository.findByUsername(Username.of("sa2"))).isNotNull();
        assertThat(this.usersRepository.findByUsername(Username.of("sa4"))).isNull();
        assertThat(this.usersRepository.findByUsernameIn(
                Set.of(
                        Username.of("sa1"),
                        Username.of("admin"),
                        Username.of("not_real1")
                )
        )).hasSize(2);
        assertThat(this.usersRepository.findByUsernameIn(
                List.of(
                        Username.of("sa3"),
                        Username.of("user1"),
                        Username.of("not_real2")
                )
        )).hasSize(2);

        assertThat(toUsernamesAsStrings1(superadmins))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");

        assertThat(toUsernamesAsStrings1(notSuperadmins))
                .hasSize(3)
                .containsExactly("admin", "user1", "user2");

        assertThat(toUsernamesAsStrings0(superadminsUsernames))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");

        assertThat(toUsernamesAsStrings0(notSuperadminsUsernames))
                .hasSize(3)
                .containsExactly("admin", "user1", "user2");

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
        assertThat(toUsernamesAsStrings1(users1))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");

        // Act-Assert-2
        this.usersRepository.deleteByAuthoritySuperadmin();
        var users2 = this.usersRepository.findAll();
        assertThat(users2).isEmpty();
    }
}
