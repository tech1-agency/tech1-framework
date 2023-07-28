package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoBaseSuperAdminServiceTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {

        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        MongoInvitationCodesRepository invitationCodeRepository() {
            return mock(MongoInvitationCodesRepository.class);
        }

        @Bean
        MongoUsersSessionsRepository userSessionRepository() {
            return mock(MongoUsersSessionsRepository.class);
        }

        @Bean
        BaseSuperAdminService superAdminService() {
            return new MongoBaseSuperAdminService(
                    this.sessionRegistry(),
                    this.invitationCodeRepository(),
                    this.userSessionRepository()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final MongoInvitationCodesRepository mongoInvitationCodesRepository;
    private final MongoUsersSessionsRepository mongoUsersSessionsRepository;

    private final BaseSuperAdminService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.mongoInvitationCodesRepository,
                this.mongoUsersSessionsRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.mongoInvitationCodesRepository,
                this.mongoUsersSessionsRepository
        );
    }

    @Test
    void findUnusedTestt() {
        // Arrange
        var invitationCodes = list345(MongoDbInvitationCode.class);
        when(this.mongoInvitationCodesRepository.findByInvitedIsNull()).thenReturn(invitationCodes);

        // Act
        var unused = this.componentUnderTest.findUnused();

        // Assert
        verify(this.mongoInvitationCodesRepository).findByInvitedIsNull();
        assertThat(unused).hasSize(invitationCodes.size());
        assertThat(unused.stream().map(ResponseInvitationCode::value).collect(Collectors.toSet()))
                .isEqualTo(invitationCodes.stream().map(MongoDbInvitationCode::getValue).collect(Collectors.toSet()));
    }

    @Test
    void getServerSessionsTest() {
        // Arrange
        var cookie = entity(CookieRefreshToken.class);

        var dbUserSession1 = entity(MongoDbUserSession.class);
        var dbUserSession2 = entity(MongoDbUserSession.class);
        var dbUserSession3 = entity(MongoDbUserSession.class);

        when(this.mongoUsersSessionsRepository.findAll()).thenReturn(asList(dbUserSession1, dbUserSession2, dbUserSession3));
        var activeSessions = Set.of(
                dbUserSession1.getJwtRefreshToken(),
                dbUserSession3.getJwtRefreshToken()
        );

        when(this.sessionRegistry.getActiveSessionsRefreshTokens()).thenReturn(activeSessions);

        // Act
        var serverSessions = this.componentUnderTest.getServerSessions(cookie);

        // Assert
        verify(this.mongoUsersSessionsRepository).findAll();
        verify(this.sessionRegistry).getActiveSessionsRefreshTokens();
        assertThat(serverSessions).isNotNull();
        assertThat(serverSessions.activeSessions()).hasSize(2);
        assertThat(serverSessions.getActiveUsernames()).containsExactlyInAnyOrder(dbUserSession1.getUsername(), dbUserSession3.getUsername());
        assertThat(serverSessions.inactiveSessions()).hasSize(1);
        assertThat(serverSessions.getInactiveUsernames()).containsExactlyInAnyOrder(dbUserSession2.getUsername());
    }
}
