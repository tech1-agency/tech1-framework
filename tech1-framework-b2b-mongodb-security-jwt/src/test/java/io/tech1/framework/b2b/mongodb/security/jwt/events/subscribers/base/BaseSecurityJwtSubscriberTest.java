package io.tech1.framework.b2b.mongodb.security.jwt.events.subscribers.base;

import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionSessionRefreshedEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.events.subscribers.SecurityJwtSubscriber;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserEmailService;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
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
        UserEmailService userEmailService() {
            return mock(UserEmailService.class);
        }

        @Bean
        UserSessionService userSessionService() {
            return mock(UserSessionService.class);
        }

        @Bean
        SecurityJwtSubscriber securityJwtSubscriber() {
            return new BaseSecurityJwtSubscriber(
                    this.securityJwtIncidentPublisher(),
                    this.userEmailService(),
                    this.userSessionService()
            );
        }
    }

    // Publishers
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UserEmailService userEmailService;
    private final UserSessionService userSessionService;

    private final SecurityJwtSubscriber componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtIncidentPublisher,
                this.userEmailService,
                this.userSessionService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtIncidentPublisher,
                this.userEmailService,
                this.userSessionService
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
    void onSessionAddUserRequestMetadataNotAuthenticationEndpointTest() {
        // Arrange
        var event = new EventSessionAddUserRequestMetadata(
                randomUsername(),
                randomEmail(),
                entity(UserSessionId.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                false,
                false
        );
        var userSession = entity(MongoDbUserSession.class);
        when(this.userSessionService.saveUserRequestMetadata(event)).thenReturn(userSession);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.userSessionService).saveUserRequestMetadata(event);
    }

    @Test
    void onSessionAddUserRequestMetadataIsAuthenticationLoginEndpointTest() {
        // Arrange
        var event = new EventSessionAddUserRequestMetadata(
                randomUsername(),
                randomEmail(),
                entity(UserSessionId.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                true,
                false
        );
        var userSession = entity(MongoDbUserSession.class);
        when(this.userSessionService.saveUserRequestMetadata(event)).thenReturn(userSession);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.userSessionService).saveUserRequestMetadata(event);
        verify(this.userEmailService).executeAuthenticationLogin(new FunctionAuthenticationLoginEmail(event.username(), event.email(), userSession.getRequestMetadata()));
        verify(this.securityJwtIncidentPublisher).publishAuthenticationLogin(new IncidentAuthenticationLogin(event.username(), userSession.getRequestMetadata()));
    }

    @Test
    void onSessionAddUserRequestMetadataIsAuthenticationRefreshTokenEndpointTest() {
        // Arrange
        var event = new EventSessionAddUserRequestMetadata(
                randomUsername(),
                randomEmail(),
                entity(UserSessionId.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                false,
                true
        );
        var userSession = entity(MongoDbUserSession.class);
        when(this.userSessionService.saveUserRequestMetadata(event)).thenReturn(userSession);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.userSessionService).saveUserRequestMetadata(event);
        verify(this.userEmailService).executeSessionRefreshed(new FunctionSessionRefreshedEmail(event.username(), event.email(), userSession.getRequestMetadata()));
        verify(this.securityJwtIncidentPublisher).publishSessionRefreshed(new IncidentSessionRefreshed(event.username(), userSession.getRequestMetadata()));
    }
}
