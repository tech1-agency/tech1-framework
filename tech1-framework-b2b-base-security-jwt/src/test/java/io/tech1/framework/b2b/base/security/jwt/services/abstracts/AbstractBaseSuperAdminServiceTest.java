package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.tuples.Tuple2;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseSuperAdminServiceTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {

        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        AnyDbInvitationCodesRepository anyDbInvitationCodesRepository() {
            return mock(AnyDbInvitationCodesRepository.class);
        }

        @Bean
        AnyDbUsersSessionsRepository anyDbUsersSessionsRepository() {
            return mock(AnyDbUsersSessionsRepository.class);
        }

        @Bean
        AbstractBaseSuperAdminService abstractBaseInvitationCodesService() {
            return new AbstractBaseSuperAdminService(
                    this.sessionRegistry(),
                    this.anyDbInvitationCodesRepository(),
                    this.anyDbUsersSessionsRepository()
            ) {};
        }
    }

    // Sessions
    private final SessionRegistry sessionRegistry;
    // Repositories
    private final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    private final AnyDbUsersSessionsRepository anyDbUsersSessionsRepository;

    private final AbstractBaseSuperAdminService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.anyDbInvitationCodesRepository,
                this.anyDbUsersSessionsRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.anyDbInvitationCodesRepository,
                this.anyDbUsersSessionsRepository
        );
    }

    @Test
    void findUnusedTest() {
        // Arrange
        var invitationCodes = list345(ResponseInvitationCode.class);
        when(this.anyDbInvitationCodesRepository.findUnused()).thenReturn(invitationCodes);

        // Act
        var unused = this.componentUnderTest.findUnused();

        // Assert
        verify(this.anyDbInvitationCodesRepository).findUnused();
        assertThat(unused).isEqualTo(invitationCodes);
    }

    @Test
    void getServerSessionsTest() {
        // Arrange
        var cookie = entity(CookieRefreshToken.class);

        var session1 = entity(ResponseUserSession2.class);
        var session2 = entity(ResponseUserSession2.class);
        var session3 = entity(ResponseUserSession2.class);

        var token1 = entity(JwtRefreshToken.class);
        var token2 = entity(JwtRefreshToken.class);
        var token3 = entity(JwtRefreshToken.class);

        when(this.anyDbUsersSessionsRepository.findAllByCookieAsSession2(cookie)).thenReturn(
                new ArrayList<>(
                        List.of(
                                new Tuple2<>(session1, token1),
                                new Tuple2<>(session2, token2),
                                new Tuple2<>(session3, token3)
                        )
                )
        );
        var activeSessions = Set.of(token1, token3);
        when(this.sessionRegistry.getActiveSessionsRefreshTokens()).thenReturn(activeSessions);

        // Act
        var serverSessions = this.componentUnderTest.getServerSessions(cookie);

        // Assert
        verify(this.anyDbUsersSessionsRepository).findAllByCookieAsSession2(cookie);
        verify(this.sessionRegistry).getActiveSessionsRefreshTokens();
        assertThat(serverSessions).isNotNull();
        assertThat(serverSessions.activeSessions()).hasSize(2);
        assertThat(serverSessions.getActiveUsernames()).containsExactlyInAnyOrder(session1.who(), session3.who());
        assertThat(serverSessions.inactiveSessions()).hasSize(1);
        assertThat(serverSessions.getInactiveUsernames()).containsExactlyInAnyOrder(session2.who());
    }
}
