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
    void findByInvitedTests() {
        // Arrange
        this.invitationCodeRepository.saveAll(dummyInvitationCodesData1());

        // Act
        var used = this.invitationCodeRepository.findByInvitedAlreadyUsed();
        var notUsed = this.invitationCodeRepository.findByInvitedNotUsed();

        // Assert
        assertThat(used).hasSize(1);
        assertThat(notUsed).hasSize(5);
    }

    @Test
    void deleteByInvitedTests() {
        // Arrange
        this.invitationCodeRepository.saveAll(dummyInvitationCodesData1());

        // Act-1
        this.invitationCodeRepository.deleteByInvitedAlreadyUsed();

        // Assert-1
        assertThat(this.invitationCodeRepository.count()).isEqualTo(5);

        // Act-2
        this.invitationCodeRepository.deleteByInvitedNotUsed();

        // Assert-2
        assertThat(this.invitationCodeRepository.count()).isZero();
    }
}
