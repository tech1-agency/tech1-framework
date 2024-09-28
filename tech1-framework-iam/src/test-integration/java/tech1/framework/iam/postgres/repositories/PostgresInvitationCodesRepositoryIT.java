package tech1.framework.iam.postgres.repositories;

import tech1.framework.iam.postgres.configs.PostgresBeforeAllCallback;
import tech1.framework.iam.postgres.configs.TestsApplicationPostgresRepositoriesRunner;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.tuples.TuplePresence;
import tech1.framework.iam.configurations.ApplicationPostgresRepositories;
import tech1.framework.iam.domain.db.InvitationCode;
import tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import tech1.framework.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.iam.domain.postgres.db.PostgresDbInvitationCode;
import tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import static tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomElement;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static tech1.framework.iam.domain.db.InvitationCode.INVITATION_CODES_UNUSED;
import static tech1.framework.iam.tests.random.postgres.PostgresSecurityJwtDbDummies.dummyInvitationCodesData1;
import static tech1.framework.iam.tests.random.postgres.PostgresSecurityJwtDbDummies.dummyInvitationCodesData2;
import static tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith({
        PostgresBeforeAllCallback.class
})
@SpringBootTest(
        webEnvironment = NONE,
        classes = {
                ApplicationPostgresRepositories.class
        }
)
@AutoConfigureDataJpa
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresInvitationCodesRepositoryIT extends TestsApplicationPostgresRepositoriesRunner {

    private final PostgresInvitationCodesRepository invitationCodesRepository;

    @Override
    public JpaRepository<PostgresDbInvitationCode, String> getJpaRepository() {
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
        var saved = this.invitationCodesRepository.saveAll(dummyInvitationCodesData1());

        var notExistentInvitationCodeId = entity(InvitationCodeId.class);
        var notExistentInvitationCode = randomStringLetterOrNumbersOnly(InvitationCode.DEFAULT_INVITATION_CODE_LENGTH);

        var savedInvitationCode = saved.get(0);
        var existentInvitationCodeId = savedInvitationCode.invitationCodeId();
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
        this.invitationCodesRepository.saveAll(dummyInvitationCodesData2());

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
        var saved = this.invitationCodesRepository.saveAll(dummyInvitationCodesData1());
        var notExistentInvitationCodeId = entity(InvitationCodeId.class);
        var existentInvitationCodeId = saved.get(0).invitationCodeId();

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
        var saved = this.invitationCodesRepository.saveAll(dummyInvitationCodesData1());
        var request = RequestNewInvitationCodeParams.random();

        // Act-Assert-0
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationCodesRepository.saveAs(randomElement(saved).invitationCode());
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-2
        var existentInvitationCodeId = this.invitationCodesRepository.saveAs(InvitationCode.random());
        assertThat(this.invitationCodesRepository.count()).isEqualTo(7);
        var notExistentInvitationCodeId = entity(InvitationCodeId.class);
        assertThat(this.invitationCodesRepository.isPresent(existentInvitationCodeId).present()).isTrue();
        assertThat(this.invitationCodesRepository.isPresent(notExistentInvitationCodeId).present()).isFalse();

        // Act-Assert-3
        this.invitationCodesRepository.saveAs(Username.testsHardcoded(), request);
        assertThat(this.invitationCodesRepository.count()).isEqualTo(8);
        var ownedInvitationCodes = this.invitationCodesRepository.findByOwner(Username.testsHardcoded());
        assertThat(ownedInvitationCodes).hasSize(1);
        var ownedInvitationCode = ownedInvitationCodes.get(0);
        assertThat(ownedInvitationCode.getOwner()).isEqualTo(Username.testsHardcoded());
        assertThat(ownedInvitationCode.getAuthorities()).isEqualTo(getSimpleGrantedAuthorities(request.authorities()));
        assertThat(ownedInvitationCode.getValue()).hasSize(40);
    }
}
