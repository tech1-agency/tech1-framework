package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbDummies.dummyInvitationCodesData1;
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
        assertThat(this.postgresInvitationCodesRepository.findByOwner(Username.of("user3"))).hasSize(1);
        assertThat(this.postgresInvitationCodesRepository.findByOwner(Username.of("user5"))).isEmpty();
        assertThat(this.postgresInvitationCodesRepository.findByInvitedIsNull()).hasSize(5);
        assertThat(this.postgresInvitationCodesRepository.findByInvitedIsNotNull()).hasSize(1);
        assertThat(this.postgresInvitationCodesRepository.countByOwner(Username.of("user1"))).isEqualTo(2);
        assertThat(this.postgresInvitationCodesRepository.countByOwner(Username.of("user2"))).isEqualTo(3);
        assertThat(this.postgresInvitationCodesRepository.countByOwner(Username.of("user3"))).isEqualTo(1);
        assertThat(this.postgresInvitationCodesRepository.countByOwner(Username.of("user5"))).isZero();
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.postgresInvitationCodesRepository.saveAll(dummyInvitationCodesData1());

        // Act-Assert-0
        assertThat(this.postgresInvitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.postgresInvitationCodesRepository.deleteByInvitedIsNotNull();
        assertThat(this.postgresInvitationCodesRepository.count()).isEqualTo(5);

        // Act-Assert-2
        this.postgresInvitationCodesRepository.deleteByInvitedIsNull();
        assertThat(this.postgresInvitationCodesRepository.count()).isZero();
    }
}
