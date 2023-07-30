package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
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

    private final MongoInvitationCodesRepository invitationCodesRepository;

    @Override
    public MongoRepository<MongoDbInvitationCode, String> getMongoRepository() {
        return this.invitationCodesRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.invitationCodesRepository.saveAll(dummyInvitationCodesData1());

        // Act
        var count = this.invitationCodesRepository.count();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.invitationCodesRepository.findByOwner(Username.of("user1"))).hasSize(2);
        assertThat(this.invitationCodesRepository.findByOwner(Username.of("user2"))).hasSize(3);
        assertThat(this.invitationCodesRepository.findByOwner(Username.of("user3"))).hasSize(1);
        assertThat(this.invitationCodesRepository.findByOwner(Username.of("user5"))).isEmpty();
        assertThat(this.invitationCodesRepository.findByInvitedNotUsed()).hasSize(5);
        assertThat(this.invitationCodesRepository.findByInvitedAlreadyUsed()).hasSize(1);
        assertThat(this.invitationCodesRepository.findByInvitedIsNull()).hasSize(5);
        assertThat(this.invitationCodesRepository.findByInvitedIsNotNull()).hasSize(1);
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user1"))).isEqualTo(2);
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user2"))).isEqualTo(3);
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user3"))).isEqualTo(1);
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user5"))).isZero();
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.invitationCodesRepository.saveAll(dummyInvitationCodesData1());

        // Act-Assert-0
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationCodesRepository.deleteByInvitedAlreadyUsed();
        assertThat(this.invitationCodesRepository.count()).isEqualTo(5);

        // Act-Assert-2
        this.invitationCodesRepository.deleteByInvitedNotUsed();
        assertThat(this.invitationCodesRepository.count()).isZero();
    }
}
