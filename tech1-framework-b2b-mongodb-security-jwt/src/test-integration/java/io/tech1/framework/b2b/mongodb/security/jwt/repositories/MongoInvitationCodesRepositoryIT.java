package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.TestsApplicationRepositoriesRunner;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.MongoSecurityJwtDbDummies.dummyInvitationCodesData1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                MongoInvitationCodesRepository.class
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoInvitationCodesRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final MongoInvitationCodesRepository mongoInvitationCodesRepository;

    @Override
    public MongoRepository<MongoDbInvitationCode, String> getMongoRepository() {
        return this.mongoInvitationCodesRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.mongoInvitationCodesRepository.saveAll(dummyInvitationCodesData1());

        // Act
        var count = this.mongoInvitationCodesRepository.count();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.mongoInvitationCodesRepository.findByInvitedAlreadyUsed()).hasSize(1);
        assertThat(this.mongoInvitationCodesRepository.findByInvitedNotUsed()).hasSize(5);
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.mongoInvitationCodesRepository.saveAll(dummyInvitationCodesData1());

        // Act-Assert-0
        assertThat(this.mongoInvitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.mongoInvitationCodesRepository.deleteByInvitedAlreadyUsed();
        assertThat(this.mongoInvitationCodesRepository.count()).isEqualTo(5);

        // Act-Assert-2
        this.mongoInvitationCodesRepository.deleteByInvitedNotUsed();
        assertThat(this.mongoInvitationCodesRepository.count()).isZero();
    }
}
