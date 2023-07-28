package io.tech1.framework.b2b.base.security.jwt.events.subscribers.base;

import io.tech1.framework.b2b.base.security.jwt.domain.events.*;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionSessionRefreshedEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.subscribers.SecurityJwtSubscriber;
import io.tech1.framework.b2b.base.security.jwt.services.DeleteService;
import io.tech1.framework.b2b.base.security.jwt.services.UserEmailService;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
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
        DeleteService deleteService() {
            return mock(DeleteService.class);
        }

        @Bean
        SecurityJwtSubscriber securityJwtSubscriber() {
            return new BaseSecurityJwtSubscriber(
                    this.securityJwtIncidentPublisher(),
                    this.userEmailService(),
                    this.deleteService()
            );
        }
    }

    // Publishers
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UserEmailService userEmailService;
    private final DeleteService deleteService;

    private final SecurityJwtSubscriber componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtIncidentPublisher,
                this.userEmailService,
                this.deleteService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtIncidentPublisher,
                this.userEmailService,
                this.deleteService
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
        var tuple2 = new Tuple2<>(entity(UserSessionId.class), entity(UserRequestMetadata.class));
        when(this.deleteService.saveUserRequestMetadata(event)).thenReturn(tuple2);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.deleteService).saveUserRequestMetadata(event);
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
        var tuple2 = new Tuple2<>(entity(UserSessionId.class), entity(UserRequestMetadata.class));
        when(this.deleteService.saveUserRequestMetadata(event)).thenReturn(tuple2);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.deleteService).saveUserRequestMetadata(event);
        verify(this.userEmailService).executeAuthenticationLogin(new FunctionAuthenticationLoginEmail(event.username(), event.email(), tuple2.b()));
        verify(this.securityJwtIncidentPublisher).publishAuthenticationLogin(new IncidentAuthenticationLogin(event.username(), tuple2.b()));
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
        var tuple2 = new Tuple2<>(entity(UserSessionId.class), entity(UserRequestMetadata.class));
        when(this.deleteService.saveUserRequestMetadata(event)).thenReturn(tuple2);

        // Act
        this.componentUnderTest.onSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.deleteService).saveUserRequestMetadata(event);
        verify(this.userEmailService).executeSessionRefreshed(new FunctionSessionRefreshedEmail(event.username(), event.email(), tuple2.b()));
        verify(this.securityJwtIncidentPublisher).publishSessionRefreshed(new IncidentSessionRefreshed(event.username(), tuple2.b()));
    }
}
