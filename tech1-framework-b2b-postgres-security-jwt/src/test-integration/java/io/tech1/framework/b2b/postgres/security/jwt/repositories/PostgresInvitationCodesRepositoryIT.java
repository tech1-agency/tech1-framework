package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.constants.SecurityJwtConstants.DEFAULT_INVITATION_CODE_LENGTH;
import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.randomInvitationCode;
import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbDummies.dummyInvitationCodesData1;
import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbDummies.dummyInvitationCodesData2;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
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

    private final PostgresInvitationCodesRepository invitationCodesRepository;

    @Override
    public JpaRepository<PostgresDbInvitationCode, String> getJpaRepository() {
        return this.invitationCodesRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        var saved = this.invitationCodesRepository.saveAll(dummyInvitationCodesData1());

        var notExistentInvitationCodeId = entity(InvitationCodeId.class);
        var notExistentInvitationCode = randomStringLetterOrNumbersOnly(DEFAULT_INVITATION_CODE_LENGTH);

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
        var requestNewInvitationCodeParams = new RequestNewInvitationCodeParams(new HashSet<>(randomStringsAsList(3)));

        // Act-Assert-0
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationCodesRepository.saveAs(randomElement(saved).invitationCode());
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-2
        var existentInvitationCodeId = this.invitationCodesRepository.saveAs(randomInvitationCode());
        assertThat(this.invitationCodesRepository.count()).isEqualTo(7);
        var notExistentInvitationCodeId = entity(InvitationCodeId.class);
        assertThat(this.invitationCodesRepository.isPresent(existentInvitationCodeId).present()).isTrue();
        assertThat(this.invitationCodesRepository.isPresent(notExistentInvitationCodeId).present()).isFalse();

        // Act-Assert-3
        this.invitationCodesRepository.saveAs(TECH1, requestNewInvitationCodeParams);
        assertThat(this.invitationCodesRepository.count()).isEqualTo(8);
        var ownedInvitationCodes = this.invitationCodesRepository.findByOwner(TECH1);
        assertThat(ownedInvitationCodes).hasSize(1);
        var ownedInvitationCode = ownedInvitationCodes.get(0);
        assertThat(ownedInvitationCode.getOwner()).isEqualTo(TECH1);
        assertThat(ownedInvitationCode.getAuthorities()).isEqualTo(requestNewInvitationCodeParams.authorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        assertThat(ownedInvitationCode.getValue()).hasSize(40);
    }
}
