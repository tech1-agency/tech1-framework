package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
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
        var cookie = entity(CookieAccessToken.class);
        var activeSessions = Set.of(entity(JwtAccessToken.class), entity(JwtAccessToken.class));
        var serverSessionsTable = entity(ResponseServerSessionsTable.class);

        when(this.sessionRegistry.getActiveSessionsAccessTokens()).thenReturn(activeSessions);
        when(this.anyDbUsersSessionsRepository.findAllByCookieAsSession2(activeSessions, cookie)).thenReturn(serverSessionsTable);


        // Act
        var actual = this.componentUnderTest.getServerSessions(cookie);

        // Assert
        verify(this.sessionRegistry).getActiveSessionsAccessTokens();
        verify(this.anyDbUsersSessionsRepository).findAllByCookieAsSession2(activeSessions, cookie);
        assertThat(actual).isEqualTo(serverSessionsTable);
    }
}
