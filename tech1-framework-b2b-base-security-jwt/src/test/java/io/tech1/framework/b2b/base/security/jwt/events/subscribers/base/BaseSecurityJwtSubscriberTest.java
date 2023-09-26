package io.tech1.framework.b2b.base.security.jwt.events.subscribers.base;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.events.*;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionSessionRefreshedEmail;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.subscribers.SecurityJwtSubscriber;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.UsersEmailsService;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogin;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
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

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityJwtSubscriberTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        SecurityJwtIncidentPublisher securityJwtIncidentPublisher() {
            return mock(SecurityJwtIncidentPublisher.class);
        }

        @Bean
        UsersEmailsService userEmailService() {
            return mock(UsersEmailsService.class);
        }

        @Bean
        BaseUsersSessionsService baseUsersSessionsService() {
            return mock(BaseUsersSessionsService.class);
        }

        @Bean
        SecurityJwtSubscriber securityJwtSubscriber() {
            return new BaseSecurityJwtSubscriber(
                    this.securityJwtIncidentPublisher(),
                    this.userEmailService(),
                    this.baseUsersSessionsService()
            );
        }
    }

    // Publishers
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UsersEmailsService usersEmailsService;
    private final BaseUsersSessionsService baseUsersSessionsService;

    private final SecurityJwtSubscriber componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtIncidentPublisher,
                this.usersEmailsService,
                this.baseUsersSessionsService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtIncidentPublisher,
                this.usersEmailsService,
                this.baseUsersSessionsService
        );
    }

    @Test
    void onAuthenticationLoginTest() {
        // Arrange
        var event = entity(EventAuthenticationLogin.class);

        // Act
        this.componentUnderTest.onAuthenticationLogin(event);

        // Assert
        verifyNoMoreInteractions(this.securityJwtIncidentPublisher);
    }

    @Test
    void onAuthenticationLoginFailureTest() {
        // Arrange
        var event = entity(EventAuthenticationLoginFailure.class);

        // Act
        this.componentUnderTest.onAuthenticationLoginFailure(event);

        // Assert
        verifyNoMoreInteractions(this.securityJwtIncidentPublisher);
    }

    @Test
    void onAuthenticationLogoutTest() {
        // Arrange
        var event = entity(EventAuthenticationLogout.class);

        // Act
        this.componentUnderTest.onAuthenticationLogout(event);

        // Assert
        verifyNoMoreInteractions(this.securityJwtIncidentPublisher);
    }

    @Test
    void onRegistration1Test() {
        // Arrange
        var event = entity(EventRegistration1.class);

        // Act
        this.componentUnderTest.onRegistration1(event);

        // Assert
        verifyNoMoreInteractions(this.securityJwtIncidentPublisher);
    }

    @Test
    void onRegistration1FailureTest() {
        // Arrange
        var event = entity(EventRegistration1Failure.class);

        // Act
        this.componentUnderTest.onRegistration1Failure(event);

        // Assert
        verifyNoMoreInteractions(this.securityJwtIncidentPublisher);
    }

    @Test
    void onSessionRefreshedTest() {
        // Arrange
        var event = entity(EventSessionRefreshed.class);

        // Act
        this.componentUnderTest.onSessionRefreshed(event);

        // Assert
        verifyNoMoreInteractions(this.securityJwtIncidentPublisher);
    }

    @Test
    void onSessionExpiredTest() {
        // Arrange
        var event = entity(EventSessionExpired.class);

        // Act
        this.componentUnderTest.onSessionExpired(event);

        // Assert
        verifyNoMoreInteractions(this.securityJwtIncidentPublisher);
    }

    @Test
    void onSessionUserRequestMetadataAddNotAuthenticationEndpointTest() {
        // Arrange
        var event = new EventSessionUserRequestMetadataAdd(
                randomUsername(),
                randomEmail(),
                entity(UserSession.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                false,
                false
        );
        when(this.baseUsersSessionsService.saveUserRequestMetadata(event)).thenReturn(event.session());

        // Act
        this.componentUnderTest.onSessionUserRequestMetadataAdd(event);

        // Assert
        verify(this.baseUsersSessionsService).saveUserRequestMetadata(event);
    }

    @Test
    void onSessionUserRequestMetadataAddIsAuthenticationLoginEndpointTest() {
        // Arrange
        var event = new EventSessionUserRequestMetadataAdd(
                randomUsername(),
                randomEmail(),
                entity(UserSession.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                true,
                false
        );
        when(this.baseUsersSessionsService.saveUserRequestMetadata(event)).thenReturn(event.session());

        // Act
        this.componentUnderTest.onSessionUserRequestMetadataAdd(event);

        // Assert
        verify(this.baseUsersSessionsService).saveUserRequestMetadata(event);
        verify(this.usersEmailsService).executeAuthenticationLogin(new FunctionAuthenticationLoginEmail(event.username(), event.email(), event.session().metadata()));
        verify(this.securityJwtIncidentPublisher).publishAuthenticationLogin(new IncidentAuthenticationLogin(event.username(), event.session().metadata()));
    }

    @Test
    void onSessionUserRequestMetadataAddIsAuthenticationRefreshTokenEndpointTest() {
        // Arrange
        var event = new EventSessionUserRequestMetadataAdd(
                randomUsername(),
                randomEmail(),
                entity(UserSession.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                false,
                true
        );
        when(this.baseUsersSessionsService.saveUserRequestMetadata(event)).thenReturn(event.session());

        // Act
        this.componentUnderTest.onSessionUserRequestMetadataAdd(event);

        // Assert
        verify(this.baseUsersSessionsService).saveUserRequestMetadata(event);
        verify(this.usersEmailsService).executeSessionRefreshed(new FunctionSessionRefreshedEmail(event.username(), event.email(), event.session().metadata()));
        verify(this.securityJwtIncidentPublisher).publishSessionRefreshed(new IncidentSessionRefreshed(event.username(), event.session().metadata()));
    }

    @Test
    void onSessionUserRequestMetadataRenewCronTest() {
        // Arrange
        var event = entity(EventSessionUserRequestMetadataRenewCron.class);

        // Act
        this.componentUnderTest.onSessionUserRequestMetadataRenewCron(event);

        // Assert
        verify(this.baseUsersSessionsService).saveUserRequestMetadata(event);
    }

    @Test
    void onSessionUserRequestMetadataRenewManuallyTest() {
        // Arrange
        var event = entity(EventSessionUserRequestMetadataRenewManually.class);

        // Act
        this.componentUnderTest.onSessionUserRequestMetadataRenewManually(event);

        // Assert
        verify(this.baseUsersSessionsService).saveUserRequestMetadata(event);
    }
}
