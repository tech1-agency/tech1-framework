package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.MongoSecurityJwtDbDummies.dummyInvitationCodesData1;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringsAsList;
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
        this.invitationCodesRepository.deleteByInvitedIsNotNull();
        assertThat(this.invitationCodesRepository.count()).isEqualTo(5);

        // Act-Assert-2
        this.invitationCodesRepository.deleteByInvitedIsNull();
        assertThat(this.invitationCodesRepository.count()).isZero();
    }

    @Test
    void saveIntegrationTests() {
        // Arrange
        this.invitationCodesRepository.saveAll(dummyInvitationCodesData1());
        var requestNewInvitationCodeParams = new RequestNewInvitationCodeParams(new HashSet<>(randomStringsAsList(3)));

        // Act-Assert-0
        assertThat(this.invitationCodesRepository.count()).isEqualTo(6);

        // Act-Assert-1
        this.invitationCodesRepository.saveAs(TECH1, requestNewInvitationCodeParams);

        // Act-Assert-2
        assertThat(this.invitationCodesRepository.count()).isEqualTo(7);

        // Act-Assert-3
        var invitationCodes = this.invitationCodesRepository.findByOwner(TECH1);
        assertThat(invitationCodes).hasSize(1);
        var invitationCode = invitationCodes.get(0);
        assertThat(invitationCode.getOwner()).isEqualTo(TECH1);
        assertThat(invitationCode.getAuthorities()).isEqualTo(requestNewInvitationCodeParams.authorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        assertThat(invitationCode.getValue()).hasSize(40);
    }
}
