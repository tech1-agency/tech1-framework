package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tasks.AbstractSuperAdminResetServerTask;
import io.tech1.framework.b2b.base.security.jwt.tests.stubbers.AbstractMockService;
import io.tech1.framework.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
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

import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.randomResetServerStatus;
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
        IncidentPublisher incidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        InvitationCodesRepository invitationCodesRepository() {
            return mock(InvitationCodesRepository.class);
        }

        @Bean
        UsersSessionsRepository usersSessionsRepository() {
            return mock(UsersSessionsRepository.class);
        }

        @Bean
        AbstractMockService abstractMockService() {
            return mock(AbstractMockService.class);
        }

        @Bean
        AbstractSuperAdminResetServerTask abstractSuperAdminResetServerTask() {
            return new AbstractSuperAdminResetServerTask() {
                @Override
                public ResetServerStatus getStatus() {
                    return randomResetServerStatus();
                }

                @Override
                public void resetOnServer(JwtUser user) {
                    abstractMockService().executeInheritedMethod();
                }

                @Override
                public void publishRuntimeExceptionOnServer(RuntimeException ex) {
                    // no actions
                }
            };
        }

        @Bean
        AbstractBaseSuperAdminService abstractBaseInvitationCodesService() {
            return new AbstractBaseSuperAdminService(
                    this.incidentPublisher(),
                    this.sessionRegistry(),
                    this.invitationCodesRepository(),
                    this.usersSessionsRepository(),
                    this.abstractSuperAdminResetServerTask()
            ) {};
        }
    }

    // Incidents
    private final IncidentPublisher incidentPublisher;
    // Sessions
    private final SessionRegistry sessionRegistry;
    // Repositories
    private final InvitationCodesRepository invitationCodesRepository;
    private final UsersSessionsRepository usersSessionsRepository;
    // Tasks
    private final AbstractSuperAdminResetServerTask resetServerTask;
    // Mocks
    private final AbstractMockService abstractMockService;

    private final AbstractBaseSuperAdminService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.incidentPublisher,
                this.sessionRegistry,
                this.invitationCodesRepository,
                this.usersSessionsRepository,
                this.abstractMockService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.incidentPublisher,
                this.sessionRegistry,
                this.invitationCodesRepository,
                this.usersSessionsRepository,
                this.abstractMockService
        );
    }

    @Test
    void getResetServerStatusTest() {
        // Act
        var actual = this.componentUnderTest.getResetServerStatus();

        // Assert
        assertThat(actual).isEqualTo(randomResetServerStatus());
    }

    @Test
    void resetServerByTest() {
        // Arrange
        var user = entity(JwtUser.class);

        // Act
        this.componentUnderTest.resetServerBy(user);

        // Assert
        verify(this.incidentPublisher).publishResetServerStarted(new IncidentSystemResetServerStarted(user.username()));
        verify(this.abstractMockService).executeInheritedMethod();
        verify(this.incidentPublisher).publishResetServerCompleted(new IncidentSystemResetServerCompleted(user.username()));
    }

    @Test
    void findUnusedTest() {
        // Arrange
        var invitationCodes = list345(ResponseInvitationCode.class);
        when(this.invitationCodesRepository.findUnused()).thenReturn(invitationCodes);

        // Act
        var unused = this.componentUnderTest.findUnused();

        // Assert
        verify(this.invitationCodesRepository).findUnused();
        assertThat(unused).isEqualTo(invitationCodes);
    }

    @Test
    void getServerSessionsTest() {
        // Arrange
        var cookie = entity(CookieAccessToken.class);
        var activeSessions = Set.of(entity(JwtAccessToken.class), entity(JwtAccessToken.class));
        var serverSessionsTable = entity(ResponseSuperadminSessionsTable.class);

        when(this.sessionRegistry.getActiveSessionsAccessTokens()).thenReturn(activeSessions);
        when(this.usersSessionsRepository.getSessionsTable(activeSessions, cookie)).thenReturn(serverSessionsTable);


        // Act
        var actual = this.componentUnderTest.getSessions(cookie);

        // Assert
        verify(this.sessionRegistry).getActiveSessionsAccessTokens();
        verify(this.usersSessionsRepository).getSessionsTable(activeSessions, cookie);
        assertThat(actual).isEqualTo(serverSessionsTable);
    }
}
