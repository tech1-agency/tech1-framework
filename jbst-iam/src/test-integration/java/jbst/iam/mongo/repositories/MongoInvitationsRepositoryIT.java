package jbst.iam.mongo.repositories;

import jbst.iam.configurations.ConfigurationMongoRepositories;
import jbst.iam.domain.db.Invitation;
import jbst.iam.domain.dto.requests.RequestNewInvitationParams;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.domain.mongodb.MongoDbInvitation;
import jbst.iam.mongo.configs.MongoBeforeAllCallback;
import jbst.iam.mongo.configs.TestsConfigurationMongoRepositoriesRunner;
import jbst.iam.repositories.mongodb.MongoInvitationsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.tuples.TuplePresence;

import static jbst.iam.domain.db.Invitation.INVITATION_CODES_UNUSED;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static jbst.foundation.utilities.random.EntityUtility.entity;
import static jbst.foundation.utilities.random.RandomUtility.randomElement;
import static jbst.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

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
class MongoInvitationsRepositoryIT extends TestsConfigurationMongoRepositoriesRunner {

    private final MongoInvitationsRepository invitationsRepository;

    @Override
    public MongoRepository<MongoDbInvitation, String> getMongoRepository() {
        return this.invitationsRepository;
    }

    @Test
    void findUnusedSortTest() {
        // Assert
        assertThat(INVITATION_CODES_UNUSED).hasToString("owner: ASC,value: ASC");
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        var saved = this.invitationsRepository.saveAll(MongoDbInvitation.dummies1());

        var notExistentInvitationId = entity(InvitationId.class);
        var notExistentInvitation = randomStringLetterOrNumbersOnly(Invitation.DEFAULT_INVITATION_CODE_LENGTH);

        var savedInvitation = saved.get(0);
        var existentInvitationId = savedInvitation.invitationId();
        var existentInvitation = savedInvitation.getValue();

        // Act
        var count = this.invitationsRepository.count();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.invitationsRepository.isPresent(existentInvitationId)).isEqualTo(TuplePresence.present(savedInvitation.invitation()));
        assertThat(this.invitationsRepository.isPresent(notExistentInvitationId)).isEqualTo(TuplePresence.absent());
        assertThat(this.invitationsRepository.findResponseCodesByOwner(Username.of("user1"))).hasSize(2);
        assertThat(this.invitationsRepository.findResponseCodesByOwner(Username.of("user2"))).hasSize(3);
        assertThat(this.invitationsRepository.findResponseCodesByOwner(Username.of("user3"))).hasSize(1);
        assertThat(this.invitationsRepository.findResponseCodesByOwner(Username.of("user5"))).isEmpty();
        assertThat(this.invitationsRepository.findByValueAsAny(notExistentInvitation)).isNull();
        assertThat(this.invitationsRepository.findByValueAsAny(existentInvitation)).isNotNull();
        assertThat(this.invitationsRepository.countByOwner(Username.of("user1"))).isEqualTo(2);
        assertThat(this.invitationsRepository.countByOwner(Username.of("user2"))).isEqualTo(3);
        assertThat(this.invitationsRepository.countByOwner(Username.of("user3"))).isEqualTo(1);
        assertThat(this.invitationsRepository.countByOwner(Username.of("user5"))).isZero();
        assertThat(this.invitationsRepository.findByInvitedIsNull()).hasSize(5);
        assertThat(this.invitationsRepository.findByInvitedIsNotNull()).hasSize(1);
    }

    @Test
    void findUnusedAndSortingTests() {
        // Arrange
        this.invitationsRepository.saveAll(MongoDbInvitation.dummies2());

        // Act
        var count = this.invitationsRepository.count();

        // Assert
        assertThat(count).isEqualTo(7);
        var unused = this.invitationsRepository.findUnused();
        assertThat(unused).hasSize(5);
        assertThat(unused.get(0).value()).isEqualTo("value111");
        assertThat(unused.get(1).value()).isEqualTo("value222");
        assertThat(unused.get(2).value()).isEqualTo("abc");
        assertThat(unused.get(3).value()).isEqualTo("value22");
        assertThat(unused.get(4).value()).isEqualTo("value44");
        var codes = this.invitationsRepository.findByInvitedIsNotNull(Sort.by("value").descending());
        assertThat(codes).hasSize(2);
        assertThat(codes.get(0).getValue()).isEqualTo("value234");
        assertThat(codes.get(1).getValue()).isEqualTo("value123");
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        var saved = this.invitationsRepository.saveAll(MongoDbInvitation.dummies1());
        var notExistentInvitationCodeId = entity(InvitationId.class);
        var existentInvitationCodeId = saved.get(0).invitationId();

        // Act-Assert-0
        assertThat(this.invitationsRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationsRepository.delete(notExistentInvitationCodeId);
        assertThat(this.invitationsRepository.count()).isEqualTo(6);

        // Act-Assert-2
        this.invitationsRepository.delete(existentInvitationCodeId);
        assertThat(this.invitationsRepository.count()).isEqualTo(5);

        // Act-Assert-1
        this.invitationsRepository.deleteByInvitedIsNotNull();
        assertThat(this.invitationsRepository.count()).isEqualTo(4);

        // Act-Assert-2
        this.invitationsRepository.deleteByInvitedIsNull();
        assertThat(this.invitationsRepository.count()).isZero();
    }

    @Test
    void saveIntegrationTests() {
        // Arrange
        var saved = this.invitationsRepository.saveAll(MongoDbInvitation.dummies1());
        var request = RequestNewInvitationParams.random();

        // Act-Assert-0
        assertThat(this.invitationsRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationsRepository.saveAs(randomElement(saved).invitation());
        assertThat(this.invitationsRepository.count()).isEqualTo(6);

        // Act-Assert-2
        var existentInvitationCodeId = this.invitationsRepository.saveAs(Invitation.random());
        assertThat(this.invitationsRepository.count()).isEqualTo(7);
        var notExistentInvitationCodeId = entity(InvitationId.class);
        assertThat(this.invitationsRepository.isPresent(existentInvitationCodeId).present()).isTrue();
        assertThat(this.invitationsRepository.isPresent(notExistentInvitationCodeId).present()).isFalse();

        // Act-Assert-3
        this.invitationsRepository.saveAs(Username.hardcoded(), request);
        assertThat(this.invitationsRepository.count()).isEqualTo(8);
        var ownedInvitationCodes = this.invitationsRepository.findByOwner(Username.hardcoded());
        assertThat(ownedInvitationCodes).hasSize(1);
        var ownedInvitationCode = ownedInvitationCodes.get(0);
        assertThat(ownedInvitationCode.getOwner()).isEqualTo(Username.hardcoded());
        assertThat(ownedInvitationCode.getAuthorities()).isEqualTo(getSimpleGrantedAuthorities(request.authorities()));
        assertThat(ownedInvitationCode.getValue()).hasSize(40);
    }
}
