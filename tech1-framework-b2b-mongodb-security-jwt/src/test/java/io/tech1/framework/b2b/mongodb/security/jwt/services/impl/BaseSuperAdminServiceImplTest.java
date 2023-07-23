package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
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
class BaseSuperAdminServiceImplTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {

        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        InvitationCodeRepository invitationCodeRepository() {
            return mock(InvitationCodeRepository.class);
        }

        @Bean
        UserSessionRepository userSessionRepository() {
            return mock(UserSessionRepository.class);
        }

        @Bean
        BaseSuperAdminService superAdminService() {
            return new BaseSuperAdminServiceImpl(
                    this.sessionRegistry(),
                    this.invitationCodeRepository(),
                    this.userSessionRepository()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final InvitationCodeRepository invitationCodeRepository;
    private final UserSessionRepository userSessionRepository;

    private final BaseSuperAdminService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.invitationCodeRepository,
                this.userSessionRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.invitationCodeRepository,
                this.userSessionRepository
        );
    }

    @Test
    void findUnusedTestt() {
        // Arrange
        var invitationCodes = list345(DbInvitationCode.class);
        when(this.invitationCodeRepository.findByInvitedIsNull()).thenReturn(invitationCodes);

        // Act
        var unused = this.componentUnderTest.findUnused();

        // Assert
        verify(this.invitationCodeRepository).findByInvitedIsNull();
        assertThat(unused).hasSize(invitationCodes.size());
        assertThat(unused.stream().map(ResponseInvitationCode1::getValue).collect(Collectors.toSet()))
                .isEqualTo(invitationCodes.stream().map(DbInvitationCode::getValue).collect(Collectors.toSet()));
    }

    @Test
    void getServerSessionsTest() {
        // Arrange
        var dbUserSession1 = entity(DbUserSession.class);
        var dbUserSession2 = entity(DbUserSession.class);
        var dbUserSession3 = entity(DbUserSession.class);

        when(this.userSessionRepository.findAll()).thenReturn(asList(dbUserSession1, dbUserSession2, dbUserSession3));
        var activeSessions = Set.of(
                dbUserSession1.getJwtRefreshToken(),
                dbUserSession3.getJwtRefreshToken()
        );

        when(this.sessionRegistry.getActiveSessionsRefreshTokens()).thenReturn(activeSessions);

        // Act
        var serverSessions = this.componentUnderTest.getServerSessions();

        // Assert
        verify(this.userSessionRepository).findAll();
        verify(this.sessionRegistry).getActiveSessionsRefreshTokens();
        assertThat(serverSessions).isNotNull();
        assertThat(serverSessions.activeSessions()).hasSize(2);
        assertThat(serverSessions.getActiveUsernames()).containsExactlyInAnyOrder(dbUserSession1.getUsername(), dbUserSession3.getUsername());
        assertThat(serverSessions.inactiveSessions()).hasSize(1);
        assertThat(serverSessions.getInactiveUsernames()).containsExactlyInAnyOrder(dbUserSession2.getUsername());
    }
}
