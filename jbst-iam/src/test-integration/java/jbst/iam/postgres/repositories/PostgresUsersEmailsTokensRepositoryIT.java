package jbst.iam.postgres.repositories;

import jbst.foundation.utilities.random.RandomUtility;
import jbst.iam.configurations.ConfigurationPostgresRepositories;
import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.dto.requests.RequestUserEmailToken;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.postgres.db.PostgresDbUserEmailToken;
import jbst.iam.postgres.configs.PostgresBeforeAllCallback;
import jbst.iam.postgres.configs.TestsConfigurationPostgresRepositoriesRunner;
import jbst.iam.repositories.postgres.PostgresUsersEmailsTokensRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import static jbst.foundation.utilities.random.EntityUtility.entity;
import static jbst.foundation.utilities.random.RandomUtility.randomElement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith({
        PostgresBeforeAllCallback.class
})
@SpringBootTest(
        webEnvironment = NONE,
        classes = {
                ConfigurationPostgresRepositories.class
        }
)
@AutoConfigureDataJpa
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresUsersEmailsTokensRepositoryIT extends TestsConfigurationPostgresRepositoriesRunner {

    private final PostgresUsersEmailsTokensRepository usersEmailsTokensRepository;

    @Override
    public JpaRepository<?, String> getJpaRepository() {
        return this.usersEmailsTokensRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        var saved = this.usersEmailsTokensRepository.saveAll(PostgresDbUserEmailToken.dummies1());

        var notExistentTokenId = entity(TokenId.class);
        var notExistentToken = RandomUtility.randomString();

        var savedToken = saved.get(0);
        var existentTokenId = savedToken.tokenId();
        var existentToken = savedToken.getValue();
        var savedExpiredToken = saved.get(3);
        var expiredTokenId = savedExpiredToken.tokenId();
        var expiredToken = savedExpiredToken.getValue();

        // Act
        var count = this.usersEmailsTokensRepository.count();

        // Assert
        assertThat(count).isEqualTo(4);
        assertThat(this.usersEmailsTokensRepository.findById(existentTokenId.value())).isNotEmpty();
        assertThat(this.usersEmailsTokensRepository.findById(notExistentTokenId.value())).isEmpty();
        assertThat(this.usersEmailsTokensRepository.findByValueAsAny(existentToken)).isNotNull();
        assertThat(this.usersEmailsTokensRepository.findByValueAsAny(notExistentToken)).isNull();
        assertThat(this.usersEmailsTokensRepository.findById(expiredTokenId.value())).isNotEmpty();
        assertThat(this.usersEmailsTokensRepository.findByValueAsAny(expiredToken)).isNotNull();
        this.usersEmailsTokensRepository.cleanupExpired();
        assertThat(this.usersEmailsTokensRepository.count()).isEqualTo(2);
        assertThat(this.usersEmailsTokensRepository.findById(expiredTokenId.value())).isEmpty();
        assertThat(this.usersEmailsTokensRepository.findByValueAsAny(expiredToken)).isNull();
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        var saved = this.usersEmailsTokensRepository.saveAll(PostgresDbUserEmailToken.dummies1());
        var savedExpiredToken = saved.get(3);
        var expiredTokenId = savedExpiredToken.tokenId();
        var expiredToken = savedExpiredToken.getValue();

        // Act-Assert-0
        assertThat(this.usersEmailsTokensRepository.count()).isEqualTo(4);

        // Act-Assert-1
        this.usersEmailsTokensRepository.cleanupExpired();
        assertThat(this.usersEmailsTokensRepository.count()).isEqualTo(2);
        assertThat(this.usersEmailsTokensRepository.findById(expiredTokenId.value())).isEmpty();
        assertThat(this.usersEmailsTokensRepository.findByValueAsAny(expiredToken)).isNull();
    }

    @Test
    void saveIntegrationTests() {
        // Arrange
        var saved = this.usersEmailsTokensRepository.saveAll(PostgresDbUserEmailToken.dummies1());

        // Act-Assert-0
        assertThat(this.usersEmailsTokensRepository.count()).isEqualTo(4);

        // Act-Assert-1
        this.usersEmailsTokensRepository.saveAs(randomElement(saved).asUserEmailToken());
        assertThat(this.usersEmailsTokensRepository.count()).isEqualTo(4);

        // Act-Assert-2
        var existentTokenId = this.usersEmailsTokensRepository.saveAs(UserEmailToken.random());
        assertThat(this.usersEmailsTokensRepository.count()).isEqualTo(5);
        var notExistentTokenId = entity(TokenId.class);
        assertThat(this.usersEmailsTokensRepository.findById(existentTokenId.value())).isNotEmpty();
        assertThat(this.usersEmailsTokensRepository.findById(notExistentTokenId.value())).isEmpty();

        // Act-Assert-3
        var requestUserEmailToken = RequestUserEmailToken.hardcoded();
        var userEmailToken = this.usersEmailsTokensRepository.saveAs(requestUserEmailToken);
        assertThat(this.usersEmailsTokensRepository.count()).isEqualTo(6);
        assertThat(this.usersEmailsTokensRepository.findById(userEmailToken.id().value())).isNotEmpty();
        assertThat(this.usersEmailsTokensRepository.findByValueAsAny(userEmailToken.value())).isNotNull();
    }
}
