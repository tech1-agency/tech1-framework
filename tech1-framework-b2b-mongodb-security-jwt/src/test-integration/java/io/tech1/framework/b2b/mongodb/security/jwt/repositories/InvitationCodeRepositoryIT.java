package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.TestsApplicationRepositoriesRunner;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtDbRandomUtility.dummyInvitationCodesData1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                InvitationCodeRepository.class
        }
)
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class InvitationCodeRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final InvitationCodeRepository invitationCodeRepository;

    @Override
    public MongoRepository<DbInvitationCode, String> getMongoRepository() {
        return this.invitationCodeRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        this.invitationCodeRepository.saveAll(dummyInvitationCodesData1());

        // Act
        var count = this.invitationCodeRepository.count();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.invitationCodeRepository.findByInvitedAlreadyUsed()).hasSize(1);
        assertThat(this.invitationCodeRepository.findByInvitedNotUsed()).hasSize(5);
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        this.invitationCodeRepository.saveAll(dummyInvitationCodesData1());

        // Act-Assert-0
        assertThat(this.invitationCodeRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationCodeRepository.deleteByInvitedAlreadyUsed();
        assertThat(this.invitationCodeRepository.count()).isEqualTo(5);

        // Act-Assert-2
        this.invitationCodeRepository.deleteByInvitedNotUsed();
        assertThat(this.invitationCodeRepository.count()).isZero();
    }
}
