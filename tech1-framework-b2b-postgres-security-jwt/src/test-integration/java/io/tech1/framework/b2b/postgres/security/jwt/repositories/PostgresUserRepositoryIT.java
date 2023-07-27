package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.converters.UserConverter.toUsernamesAsStrings0;
import static io.tech1.framework.b2b.postgres.security.jwt.tests.converters.UserConverter.toUsernamesAsStrings1;
import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDummies.dummyUsersData1;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                PostgresUserRepository.class,
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresUserRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final PostgresUserRepository postgresUserRepository;

    @SneakyThrows
    @Test
    void readIntegrationTests() {
        // Arrange
        this.postgresUserRepository.saveAll(dummyUsersData1());

        // Act
        var count = this.postgresUserRepository.count();
        var superadmins = this.postgresUserRepository.findByAuthoritySuperadmin();
        var notSuperadmins = this.postgresUserRepository.findByAuthorityNotSuperadmin();
        var superadminsUsernames = this.postgresUserRepository.findSuperadminsUsernames();
        var notSuperadminsUsernames = this.postgresUserRepository.findNotSuperadminsUsernames();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.postgresUserRepository.findByEmail(Email.of("sa1@tech1.io"))).isNotNull();
        assertThat(this.postgresUserRepository.findByEmail(Email.of("sa2@tech1.io"))).isNotNull();
        assertThat(this.postgresUserRepository.findByEmail(Email.of("sa4@tech1.io"))).isNull();
        assertThat(this.postgresUserRepository.findByUsername(Username.of("sa1"))).isNotNull();
        assertThat(this.postgresUserRepository.findByUsername(Username.of("sa2"))).isNotNull();
        assertThat(this.postgresUserRepository.findByUsername(Username.of("sa4"))).isNull();
        assertThat(this.postgresUserRepository.findByUsernameIn(
                Set.of(
                        Username.of("sa1"),
                        Username.of("admin"),
                        Username.of("not_real1")
                )
        )).hasSize(2);
        assertThat(this.postgresUserRepository.findByUsernameIn(
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
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.postgresUserRepository.saveAll(dummyUsersData1());

        // Act-Assert-0
        assertThat(this.postgresUserRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.postgresUserRepository.deleteByAuthorityNotSuperadmin();
        var users1 = this.postgresUserRepository.findAll();
        assertThat(toUsernamesAsStrings1(users1))
                .hasSize(3)
                .containsExactly("sa1", "sa2", "sa3");

        // Act-Assert-2
        this.postgresUserRepository.deleteByAuthoritySuperadmin();
        var users2 = this.postgresUserRepository.findAll();
        assertThat(users2).isEmpty();
    }
}
