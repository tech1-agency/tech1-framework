package jbst.iam.mongo.repositories;

import jbst.foundation.utilities.random.RandomUtility;
import jbst.iam.configurations.ConfigurationMongoRepositories;
import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.dto.requests.RequestUserEmailToken;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.mongodb.MongoDbUserEmailToken;
import jbst.iam.mongo.configs.MongoBeforeAllCallback;
import jbst.iam.mongo.configs.TestsConfigurationMongoRepositoriesRunner;
import jbst.iam.repositories.mongodb.MongoUsersEmailsTokensRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;

import static jbst.foundation.utilities.random.EntityUtility.entity;
import static jbst.foundation.utilities.random.RandomUtility.randomElement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith({
        MongoBeforeAllCallback.class
})
@SpringBootTest(
        webEnvironment = NONE,
        classes = {
                ConfigurationMongoRepositories.class
        }
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoUsersEmailsTokensRepositoryIT extends TestsConfigurationMongoRepositoriesRunner {

    private final MongoUsersEmailsTokensRepository usersEmailsTokensRepository;

    @Override
    public MongoRepository<?, String> getMongoRepository() {
        return this.usersEmailsTokensRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        var saved = this.usersEmailsTokensRepository.saveAll(MongoDbUserEmailToken.dummies1());

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
        var saved = this.usersEmailsTokensRepository.saveAll(MongoDbUserEmailToken.dummies1());
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
        var saved = this.usersEmailsTokensRepository.saveAll(MongoDbUserEmailToken.dummies1());

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
