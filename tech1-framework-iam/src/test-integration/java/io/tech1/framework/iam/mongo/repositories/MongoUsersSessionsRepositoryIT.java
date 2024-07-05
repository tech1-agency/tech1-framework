package io.tech1.framework.iam.mongo.repositories;

import io.tech1.framework.iam.configurations.ApplicationMongoRepositories;
import io.tech1.framework.iam.domain.mongodb.MongoDbUserSession;
import io.tech1.framework.iam.repositories.mongodb.MongoUsersSessionsRepository;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.tuples.TuplePresence;
import io.tech1.framework.iam.domain.db.UserSession;
import io.tech1.framework.iam.domain.identifiers.UserSessionId;
import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtRefreshToken;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.mongo.configs.MongoBeforeAllCallback;
import io.tech1.framework.iam.mongo.configs.TestsApplicationMongoRepositoriesRunner;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

import static io.tech1.framework.iam.tests.converters.mongodb.MongoUserConverter.toAccessTokensAsStrings2;
import static io.tech1.framework.iam.tests.converters.mongodb.MongoUserConverter.toUsernamesAsStrings2;
import static io.tech1.framework.iam.tests.converters.mongodb.MongoUserSessionConverter.toMetadataRenewCron;
import static io.tech1.framework.iam.tests.random.mongodb.MongoSecurityJwtDbDummies.dummyUserSessionsData1;
import static io.tech1.framework.iam.tests.random.mongodb.MongoSecurityJwtDbDummies.dummyUserSessionsData2;
import static io.tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomElement;
import static io.tech1.framework.iam.domain.jwt.JwtAccessToken.accessTokens;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith({
        MongoBeforeAllCallback.class
})
@SpringBootTest(
        webEnvironment = NONE,
        classes = {
                ApplicationMongoRepositories.class
        }
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoUsersSessionsRepositoryIT extends TestsApplicationMongoRepositoriesRunner {

    private final MongoUsersSessionsRepository usersSessionsRepository;

    @Override
    public MongoRepository<MongoDbUserSession, String> getMongoRepository() {
        return this.usersSessionsRepository;
    }

    @Test
    void readIntegrationTests() {
        // Arrange
        var saved = this.usersSessionsRepository.saveAll(dummyUserSessionsData1());

        var notExistentSessionId = UserSessionId.random();

        var savedSession = saved.get(0);
        var existentSessionId = savedSession.userSessionId();

        // Act
        var count = this.usersSessionsRepository.count();

        // Assert
        assertThat(count).isEqualTo(7);
        assertThat(this.usersSessionsRepository.isPresent(existentSessionId, Username.random())).isEqualTo(TuplePresence.absent());
        assertThat(this.usersSessionsRepository.isPresent(existentSessionId, savedSession.getUsername())).isEqualTo(TuplePresence.present(savedSession.userSession()));
        assertThat(this.usersSessionsRepository.isPresent(notExistentSessionId, Username.random())).isEqualTo(TuplePresence.absent());
        assertThat(this.usersSessionsRepository.isPresent(existentSessionId)).isEqualTo(TuplePresence.present(savedSession.userSession()));
        assertThat(this.usersSessionsRepository.isPresent(notExistentSessionId)).isEqualTo(TuplePresence.absent());
        assertThat(this.usersSessionsRepository.isPresent(JwtAccessToken.of("awt1")).present()).isTrue();
        assertThat(this.usersSessionsRepository.isPresent(JwtAccessToken.of("awt2")).present()).isTrue();
        assertThat(this.usersSessionsRepository.isPresent(JwtAccessToken.of("awt777")).present()).isFalse();
        assertThat(this.usersSessionsRepository.isPresent(JwtRefreshToken.of("rwt1")).present()).isTrue();
        assertThat(this.usersSessionsRepository.isPresent(JwtRefreshToken.of("rwt2")).present()).isTrue();
        assertThat(this.usersSessionsRepository.isPresent(JwtRefreshToken.of("rwt777")).present()).isFalse();
        assertThat(this.usersSessionsRepository.getUsersSessionsTable(Username.of("user777"), new RequestAccessToken("awt2"))).isEmpty();
        var usersSessions = this.usersSessionsRepository.getUsersSessionsTable(Username.testsHardcoded(), new RequestAccessToken("awt2"));
        assertThat(usersSessions).hasSize(4);
        assertThat(usersSessions.get(0).current()).isTrue();
        assertThat(usersSessions.get(0).activity()).isEqualTo("Current session");
        usersSessions.stream().skip(1).forEach(userSession -> {
            assertThat(userSession.current()).isFalse();
            assertThat(userSession.activity()).isEqualTo("—");
        });
        var sessionsTable = this.usersSessionsRepository.getSessionsTable(accessTokens("awt3", "atoken11", "atoken"), new RequestAccessToken("atoken"));
        assertThat(sessionsTable.activeSessions()).hasSize(3);
        assertThat(sessionsTable.activeSessions().get(0).current()).isTrue();
        assertThat(sessionsTable.activeSessions().get(0).who().value()).isEqualTo("sa");
        assertThat(sessionsTable.activeSessions().get(1).current()).isFalse();
        assertThat(sessionsTable.activeSessions().get(1).who().value()).isEqualTo("tech1");
        assertThat(sessionsTable.activeSessions().get(2).current()).isFalse();
        assertThat(sessionsTable.activeSessions().get(2).who().value()).isEqualTo("user1");
        assertThat(sessionsTable.inactiveSessions()).hasSize(4);
        sessionsTable.inactiveSessions().forEach(inactiveSession -> assertThat(inactiveSession.current()).isFalse());
        assertThat(sessionsTable.inactiveSessions().get(0).who().value()).isEqualTo("tech1");
        assertThat(sessionsTable.inactiveSessions().get(1).who().value()).isEqualTo("tech1");
        assertThat(sessionsTable.inactiveSessions().get(2).who().value()).isEqualTo("tech1");
        assertThat(sessionsTable.inactiveSessions().get(3).who().value()).isEqualTo("user1");
        assertThat(this.usersSessionsRepository.findByUsernameInAsAny(Set.of(Username.testsHardcoded(), Username.of("sa")))).hasSize(5);
        assertThat(this.usersSessionsRepository.findByUsernameInAsAny(Set.of(Username.testsHardcoded(), Username.of("user1")))).hasSize(6);
        assertThat(this.usersSessionsRepository.findByUsernameInAsAny(Set.of(Username.of("user1"), Username.of("sa")))).hasSize(3);
        assertThat(this.usersSessionsRepository.findByUsernameInAsAny(Set.of(Username.of("user777"), Username.of("sa777")))).isEmpty();
    }

    @Test
    void enableMetadataRenewCronTest() {
        // Arrange
        var saved1 = this.usersSessionsRepository.saveAll(dummyUserSessionsData1());

        // Assert-0
        assertThat(toMetadataRenewCron(saved1))
                .hasSize(1)
                .contains(false);

        // Act
        this.usersSessionsRepository.enableMetadataRenewCron();

        // Assert-1
        assertThat(toMetadataRenewCron(this.usersSessionsRepository.findAll()))
                .hasSize(1)
                .contains(true);
    }

    @Test
    void enableMetadataRenewManuallyTest() {
        // Arrange
        var saved1 = this.usersSessionsRepository.saveAll(dummyUserSessionsData1());

        // Assert-0
        assertThat(toMetadataRenewCron(saved1))
                .hasSize(1)
                .contains(false);
        var sessionId1 = UserSessionId.of(saved1.get(2).getId());
        var sessionId2 = UserSessionId.of(saved1.get(5).getId());

        // Act
        var session1 = this.usersSessionsRepository.enableMetadataRenewManually(sessionId1);
        var session2 = this.usersSessionsRepository.enableMetadataRenewManually(sessionId2);

        // Assert-1
        assertThat(session1).isNotNull();
        assertThat(session2).isNotNull();
        var sessions = this.usersSessionsRepository.findAll();
        sessions.forEach(session -> {
            var sessionId = session.getId();
            if (sessionId1.value().equals(sessionId) || sessionId2.value().equals(sessionId)) {
                assertThat(session.isMetadataRenewManually()).isTrue();
            } else {
                assertThat(session.isMetadataRenewManually()).isFalse();
            }
        });
    }

    @Test
    void deletionIntegrationTests() {
        // Arrange
        var saved = this.usersSessionsRepository.saveAll(dummyUserSessionsData1());

        var existentSessionId = saved.get(0).userSessionId();
        var existentSessionsIds = Set.of(saved.get(1).userSessionId(), saved.get(5).userSessionId());

        // Act-Assert-0
        assertThat(this.usersSessionsRepository.count()).isEqualTo(7);

        // Act-Assert-1
        this.usersSessionsRepository.delete(existentSessionId);
        assertThat(this.usersSessionsRepository.count()).isEqualTo(6);

        // Act-Assert-2
        this.usersSessionsRepository.delete(existentSessionsIds);
        assertThat(this.usersSessionsRepository.count()).isEqualTo(4);

        // Act-Assert-1
        this.usersSessionsRepository.deleteByUsernames(Set.of(Username.testsHardcoded(), Username.of("sa")));
        assertThat(this.usersSessionsRepository.count()).isEqualTo(1);
        assertThat(this.usersSessionsRepository.findAll().get(0).getUsername().value()).isEqualTo("user1");
    }

    @Test
    void deleteByUsernameExceptAccessTokenTest() {
        // Arrange
        this.usersSessionsRepository.saveAll(dummyUserSessionsData2());

        // Act
        var count1 = this.usersSessionsRepository.count();
        this.usersSessionsRepository.deleteByUsernameExceptAccessToken(Username.testsHardcoded(), new RequestAccessToken("token2"));
        var count2 = this.usersSessionsRepository.count();
        var sessions = this.usersSessionsRepository.findAll();
        assertThat(count1).isEqualTo(4);
        assertThat(count2).isEqualTo(2);
        assertThat(toUsernamesAsStrings2(sessions)).isEqualTo(List.of(Username.testsHardcoded().value(), "admin"));
        assertThat(toAccessTokensAsStrings2(sessions)).isEqualTo(List.of("token2", "token4"));
    }

    @Test
    void deleteExceptAccessTokenTest() {
        // Arrange
        this.usersSessionsRepository.saveAll(dummyUserSessionsData2());

        // Act
        var count1 = this.usersSessionsRepository.count();
        this.usersSessionsRepository.deleteExceptAccessToken(new RequestAccessToken("token2"));
        var count2 = this.usersSessionsRepository.count();
        var sessions = this.usersSessionsRepository.findAll();

        assertThat(count1).isEqualTo(4);
        assertThat(count2).isEqualTo(1);
        var session = sessions.get(0);
        assertThat(session.getUsername()).isEqualTo(Username.testsHardcoded());
        assertThat(session.getAccessToken().value()).isEqualTo("token2");
    }

    @Test
    void saveIntegrationTests() {
        // Arrange
        var saved = this.usersSessionsRepository.saveAll(dummyUserSessionsData1());

        // Act-Assert-0
        assertThat(this.usersSessionsRepository.count()).isEqualTo(7);

        // Act-Assert-1
        this.usersSessionsRepository.saveAs(randomElement(saved).userSession());
        assertThat(this.usersSessionsRepository.count()).isEqualTo(7);

        // Act-Assert-2
        var existentSessionId = this.usersSessionsRepository.saveAs(entity(UserSession.class)).id();
        assertThat(this.usersSessionsRepository.count()).isEqualTo(8);
        var notExistentSessionId = entity(UserSessionId.class);
        assertThat(this.usersSessionsRepository.isPresent(existentSessionId).present()).isTrue();
        assertThat(this.usersSessionsRepository.isPresent(notExistentSessionId).present()).isFalse();
    }
}
