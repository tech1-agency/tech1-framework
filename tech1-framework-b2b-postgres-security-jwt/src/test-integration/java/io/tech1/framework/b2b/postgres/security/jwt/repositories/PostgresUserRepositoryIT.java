package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        var sa1 = this.postgresUserRepository.findByUsername(Username.of("sa1"));
        var sa2 = this.postgresUserRepository.findByUsername(Username.of("sa2"));
        var sa4 = this.postgresUserRepository.findByUsername(Username.of("sa4"));

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(sa1).isNotNull();
        assertThat(sa2).isNotNull();
        assertThat(sa4).isNull();
    }
}
