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
    void integrationTests() {
        // Arrange
        this.postgresUserRepository.saveAll(dummyUsersData1());

        // Act
        var count = this.postgresUserRepository.count();

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
    }
}
