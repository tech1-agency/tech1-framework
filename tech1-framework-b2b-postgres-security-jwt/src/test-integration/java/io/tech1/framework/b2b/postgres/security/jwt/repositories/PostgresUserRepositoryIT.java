package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.stream.IntStream;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
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
    void testTest() {
        // Arrange
        var username = Username.of("tech1");
        for (int i = 0; i < 15; i++) {
            var authorities = IntStream.range(0, 3).mapToObj(index -> new SimpleGrantedAuthority(randomString())).toList();
            var user = new PostgresDbUser(username, randomPassword(), randomZoneId(), authorities);
            user.setAttributes(
                    Map.of(
                            "attr1", randomString(),
                            "attr2", randomLong()
                    )
            );
            user.setEmail(randomEmail());
            this.postgresUserRepository.save(user);
        }

        // Act
        var actual = this.postgresUserRepository.count();

        // Assert
        assertThat(actual).isEqualTo(15);
    }
}
