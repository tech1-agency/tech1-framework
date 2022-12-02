package io.tech1.framework.b2b.mongodb.security.jwt.events.subscribers.base;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;
import io.tech1.framework.b2b.mongodb.security.jwt.events.subscribers.SecurityJwtSubscriber;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.incidents.domain.authetication.AuthenticationLoginIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
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

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomIPAddress;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtSubscriberTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentPublisher incidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        UserSessionService userSessionService() {
            return mock(UserSessionService.class);
        }

        @Bean
        SecurityJwtSubscriber securityJwtSubscriber() {
            return new BaseSecurityJwtSubscriber(
                    this.incidentPublisher(),
                    this.userSessionService()
            );
        }
    }

    // Publishers
    private final IncidentPublisher incidentPublisher;
    // Services
    private final UserSessionService userSessionService;

    private final SecurityJwtSubscriber componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.incidentPublisher,
                this.userSessionService
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.incidentPublisher,
                this.userSessionService
        );
    }

    @Test
    public void onAuthenticationLoginTest() {
        // Arrange
        var event = entity(EventAuthenticationLogin.class);

        // Act
        this.componentUnderTest.onAuthenticationLogin(event);

        // Assert
        // no asserts
    }

    @Test
    public void onAuthenticationLoginFailureUsernamePasswordTest() {
        // Arrange
        var event = entity(EventAuthenticationLoginFailureUsernamePassword.class);

        // Act
        this.componentUnderTest.onAuthenticationLoginFailureUsernamePassword(event);

        // Assert
        // no asserts
    }

    @Test
    public void onAuthenticationLoginFailureUsernameMaskedPasswordTest() {
        // Arrange
        var event = entity(EventAuthenticationLoginFailureUsernameMaskedPassword.class);

        // Act
        this.componentUnderTest.onAuthenticationLoginFailureUsernameMaskedPassword(event);

        // Assert
        // no asserts
    }

    @Test
    public void onAuthenticationLogoutTest() {
        // Arrange
        var event = entity(EventAuthenticationLogout.class);

        // Act
        this.componentUnderTest.onAuthenticationLogout(event);

        // Assert
        // no asserts
    }

    @Test
    public void onRegistrationRegister1Test() {
        // Arrange
        var event = entity(EventRegistration1.class);

        // Act
        this.componentUnderTest.onRegistrationRegister1(event);

        // Assert
        // no asserts
    }

    @Test
    public void onSessionRefreshedTest() {
        // Arrange
        var event = entity(EventSessionRefreshed.class);

        // Act
        this.componentUnderTest.onSessionRefreshed(event);

        // Assert
        // no asserts
    }

    @Test
    public void onSessionExpiredTest() {
        // Arrange
        var event = entity(EventSessionExpired.class);

        // Act
        this.componentUnderTest.onSessionExpired(event);

        // Assert
        // no asserts
    }

    @Test
    public void onSessionAddUserRequestMetadataNotAuthenticationEndpointTest() {
        // Arrange
        var event = new EventSessionAddUserRequestMetadata(
                randomUsername(),
                entity(DbUserSession.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                false,
                false
        );
        var userSession = entity(DbUserSession.class);
        when(this.userSessionService.saveUserRequestMetadata(eq(event))).thenReturn(userSession);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.userSessionService).saveUserRequestMetadata(eq(event));
    }

    @Test
    public void onSessionAddUserRequestMetadataIsAuthenticationLoginEndpointTest() {
        // Arrange
        var event = new EventSessionAddUserRequestMetadata(
                randomUsername(),
                entity(DbUserSession.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                true,
                false
        );
        var userSession = entity(DbUserSession.class);
        when(this.userSessionService.saveUserRequestMetadata(eq(event))).thenReturn(userSession);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.userSessionService).saveUserRequestMetadata(eq(event));
        verify(this.incidentPublisher).publishAuthenticationLogin(eq(AuthenticationLoginIncident.of(event.getUsername(), userSession.getRequestMetadata())));
    }

    @Test
    public void onSessionAddUserRequestMetadataIsAuthenticationRefreshTokenEndpointTest() {
        // Arrange
        var event = new EventSessionAddUserRequestMetadata(
                randomUsername(),
                entity(DbUserSession.class),
                randomIPAddress(),
                mock(UserAgentHeader.class),
                false,
                true
        );
        var userSession = entity(DbUserSession.class);
        when(this.userSessionService.saveUserRequestMetadata(eq(event))).thenReturn(userSession);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.userSessionService).saveUserRequestMetadata(eq(event));
        verify(this.incidentPublisher).publishSessionRefreshed(eq(SessionRefreshedIncident.of(event.getUsername(), userSession.getRequestMetadata())));
    }
}
