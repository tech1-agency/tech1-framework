package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.TestsApplicationRepositoriesRunner;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.tuples.TuplePresence;
import io.tech1.framework.iam.domain.db.InvitationCode;
import io.tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.iam.domain.identifiers.InvitationCodeId;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.MongoSecurityJwtDbDummies.dummyInvitationCodesData1;
import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.MongoSecurityJwtDbDummies.dummyInvitationCodesData2;
import static io.tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomElement;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static io.tech1.framework.iam.comparators.SecurityJwtSorts.INVITATION_CODES_UNUSED;
import static io.tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({ SpringExtension.class })
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {
                MongoInvitationCodesRepository.class
        }
)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoInvitationCodesRepositoryIT extends TestsApplicationRepositoriesRunner {

    private final MongoInvitationCodesRepository invitationCodesRepository;

    @Override
    public MongoRepository<MongoDbInvitationCode, String> getMongoRepository() {
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
