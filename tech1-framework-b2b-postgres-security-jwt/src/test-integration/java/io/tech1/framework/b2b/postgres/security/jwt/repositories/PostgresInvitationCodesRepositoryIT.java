package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDummies.dummyInvitationCodesData1;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                PostgresInvitationCodesRepository.class,
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresInvitationCodesRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final PostgresInvitationCodesRepository postgresInvitationCodesRepository;

    @Override
    public JpaRepository<PostgresDbInvitationCode, String> getJpaRepository() {
        return this.postgresInvitationCodesRepository;
    }

    @SneakyThrows
    @Test
    void readIntegrationTests() {
        // Arrange
        this.postgresInvitationCodesRepository.saveAll(dummyInvitationCodesData1());

        // Act
        var count = this.postgresInvitationCodesRepository.count();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.postgresInvitationCodesRepository.findByOwner(Username.of("user1"))).hasSize(2);
        assertThat(this.postgresInvitationCodesRepository.findByOwner(Username.of("user2"))).hasSize(3);
        assertThat(this.postgresInvitationCodesRepository.findByOwner(Username.of("user5"))).isEmpty();
    }


}
