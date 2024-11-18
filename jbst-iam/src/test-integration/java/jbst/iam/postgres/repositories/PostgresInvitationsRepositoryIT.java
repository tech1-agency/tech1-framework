package jbst.iam.postgres.repositories;

import jbst.iam.configurations.ConfigurationPostgresRepositories;
import jbst.iam.domain.db.Invitation;
import jbst.iam.domain.dto.requests.RequestNewInvitationParams;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.domain.postgres.db.PostgresDbInvitation;
import jbst.iam.postgres.configs.PostgresBeforeAllCallback;
import jbst.iam.postgres.configs.TestsConfigurationPostgresRepositoriesRunner;
import jbst.iam.repositories.postgres.PostgresInvitationsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
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
class PostgresInvitationsRepositoryIT extends TestsConfigurationPostgresRepositoriesRunner {

    private final PostgresInvitationsRepository invitationCodesRepository;

    @Override
    public JpaRepository<PostgresDbInvitation, String> getJpaRepository() {
        return this.invitationCodesRepository;
    }

    @Test
    void findUnusedSortTest() {
        // Assert
        assertThat(INVITATION_CODES_UNUSED).hasToString("owner: ASC,value: ASC");
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        var saved = this.invitationCodesRepository.saveAll(PostgresDbInvitation.dummies1());

        var notExistentInvitationCodeId = entity(InvitationId.class);
        var notExistentInvitationCode = randomStringLetterOrNumbersOnly(Invitation.DEFAULT_INVITATION_CODE_LENGTH);

        var savedInvitationCode = saved.get(0);
        var existentInvitationCodeId = savedInvitationCode.invitationId();
        var existentInvitationCode = savedInvitationCode.getValue();

        // Act
        var count = this.invitationCodesRepository.count();

        // Assert
        assertThat(count).isEqualTo(6);
        assertThat(this.invitationCodesRepository.isPresent(existentInvitationCodeId)).isEqualTo(TuplePresence.present(savedInvitationCode.invitationCode()));
        assertThat(this.invitationCodesRepository.isPresent(notExistentInvitationCodeId)).isEqualTo(TuplePresence.absent());
        assertThat(this.invitationCodesRepository.findResponseCodesByOwner(Username.of("user1"))).hasSize(2);
        assertThat(this.invitationCodesRepository.findResponseCodesByOwner(Username.of("user2"))).hasSize(3);
        assertThat(this.invitationCodesRepository.findResponseCodesByOwner(Username.of("user3"))).hasSize(1);
        assertThat(this.invitationCodesRepository.findResponseCodesByOwner(Username.of("user5"))).isEmpty();
        assertThat(this.invitationCodesRepository.findByValueAsAny(notExistentInvitationCode)).isNull();
        assertThat(this.invitationCodesRepository.findByValueAsAny(existentInvitationCode)).isNotNull();
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user1"))).isEqualTo(2);
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user2"))).isEqualTo(3);
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user3"))).isEqualTo(1);
        assertThat(this.invitationCodesRepository.countByOwner(Username.of("user5"))).isZero();
        assertThat(this.invitationCodesRepository.findByInvitedIsNull()).hasSize(5);
        assertThat(this.invitationCodesRepository.findByInvitedIsNotNull()).hasSize(1);
    }

    @Test
    void findUnusedAndSortingTests() {
        // Arrange
        this.invitationCodesRepository.saveAll(PostgresDbInvitation.dummies2());

        // Act
        var count = this.invitationCodesRepository.count();

        // Assert
        assertThat(count).isEqualTo(7);
        var unused = this.invitationCodesRepository.findUnused();
        assertThat(unused).hasSize(5);
        assertThat(unused.get(0).value()).isEqualTo("value111");
        assertThat(unused.get(1).value()).isEqualTo("value222");
        assertThat(unused.get(2).value()).isEqualTo("abc");
        assertThat(unused.get(3).value()).isEqualTo("value22");
        assertThat(unused.get(4).value()).isEqualTo("value44");
        var codes = this.invitationCodesRepository.findByInvitedIsNotNull(Sort.by("value").descending());
        assertThat(codes).hasSize(2);
        assertThat(codes.get(0).getValue()).isEqualTo("value234");
        assertThat(codes.get(1).getValue()).isEqualTo("value123");
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        var saved = this.invitationCodesRepository.saveAll(PostgresDbInvitation.dummies1());
        var notExistentInvitationCodeId = entity(InvitationId.class);
        var existentInvitationCodeId = saved.get(0).invitationId();

        // Act-Assert-0
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationCodesRepository.delete(notExistentInvitationCodeId);
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-2
        this.invitationCodesRepository.delete(existentInvitationCodeId);
        assertThat(this.invitationCodesRepository.count()).isEqualTo(5);

        // Act-Assert-1
        this.invitationCodesRepository.deleteByInvitedIsNotNull();
        assertThat(this.invitationCodesRepository.count()).isEqualTo(4);

        // Act-Assert-2
        this.invitationCodesRepository.deleteByInvitedIsNull();
        assertThat(this.invitationCodesRepository.count()).isZero();
    }

    @Test
    void saveIntegrationTests() {
        // Arrange
        var saved = this.invitationCodesRepository.saveAll(PostgresDbInvitation.dummies1());
        var request = RequestNewInvitationParams.random();

        // Act-Assert-0
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationCodesRepository.saveAs(randomElement(saved).invitationCode());
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-2
        var existentInvitationCodeId = this.invitationCodesRepository.saveAs(Invitation.random());
        assertThat(this.invitationCodesRepository.count()).isEqualTo(7);
        var notExistentInvitationCodeId = entity(InvitationId.class);
        assertThat(this.invitationCodesRepository.isPresent(existentInvitationCodeId).present()).isTrue();
        assertThat(this.invitationCodesRepository.isPresent(notExistentInvitationCodeId).present()).isFalse();

        // Act-Assert-3
        this.invitationCodesRepository.saveAs(Username.hardcoded(), request);
        assertThat(this.invitationCodesRepository.count()).isEqualTo(8);
        var ownedInvitationCodes = this.invitationCodesRepository.findByOwner(Username.hardcoded());
        assertThat(ownedInvitationCodes).hasSize(1);
        var ownedInvitationCode = ownedInvitationCodes.get(0);
        assertThat(ownedInvitationCode.getOwner()).isEqualTo(Username.hardcoded());
        assertThat(ownedInvitationCode.getAuthorities()).isEqualTo(getSimpleGrantedAuthorities(request.authorities()));
        assertThat(ownedInvitationCode.getValue()).hasSize(40);
    }
}
