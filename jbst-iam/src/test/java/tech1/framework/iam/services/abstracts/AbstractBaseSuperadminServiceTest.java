package tech1.framework.iam.services.abstracts;

import tech1.framework.iam.tests.stubbers.AbstractMockService;
import tech1.framework.iam.domain.dto.responses.ResponseInvitationCode;
import tech1.framework.iam.domain.dto.responses.ResponseSuperadminSessionsTable;
import tech1.framework.iam.domain.jwt.JwtAccessToken;
import tech1.framework.iam.domain.jwt.JwtUser;
import tech1.framework.iam.domain.jwt.RequestAccessToken;
import tech1.framework.iam.repositories.InvitationCodesRepository;
import tech1.framework.iam.repositories.UsersSessionsRepository;
import tech1.framework.iam.sessions.SessionRegistry;
import tech1.framework.iam.tasks.superadmin.AbstractSuperAdminResetServerTask;
import tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
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

import static tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static tech1.framework.foundation.utilities.random.EntityUtility.list345;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseSuperadminServiceTest {

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
            return new AbstractSuperAdminResetServerTask(
                    this.incidentPublisher()
            ) {
                @Override
                public ResetServerStatus getStatus() {
                    return ResetServerStatus.random();
                }

                @Override
                public void resetOnServer(JwtUser initiator) {
                    abstractMockService().executeInheritedMethod();
                }
            };
        }

        @Bean
        AbstractBaseSuperadminService abstractBaseInvitationCodesService() {
            return new AbstractBaseSuperadminService(
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
    // Mocks
    private final AbstractMockService abstractMockService;

    private final AbstractBaseSuperadminService componentUnderTest;

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
        assertThat(actual).isEqualTo(ResetServerStatus.random());
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
        var requestAccessToken = RequestAccessToken.random();
        var activeSessions = Set.of(JwtAccessToken.random(), JwtAccessToken.random());
        var serverSessionsTable = entity(ResponseSuperadminSessionsTable.class);

        when(this.sessionRegistry.getActiveSessionsAccessTokens()).thenReturn(activeSessions);
        when(this.usersSessionsRepository.getSessionsTable(activeSessions, requestAccessToken)).thenReturn(serverSessionsTable);


        // Act
        var actual = this.componentUnderTest.getSessions(requestAccessToken);

        // Assert
        verify(this.sessionRegistry).getActiveSessionsAccessTokens();
        verify(this.usersSessionsRepository).getSessionsTable(activeSessions, requestAccessToken);
        assertThat(actual).isEqualTo(serverSessionsTable);
    }
}
