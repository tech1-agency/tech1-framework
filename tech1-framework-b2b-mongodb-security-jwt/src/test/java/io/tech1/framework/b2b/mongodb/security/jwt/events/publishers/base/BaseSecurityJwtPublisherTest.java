package io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.base;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtPublisherTest {

    @Configuration
    static class ContextConfiguration {
        @Primary
        @Bean
        ApplicationEventPublisher applicationEventPublisher() {
            return mock(ApplicationEventPublisher.class);
        }

        @Bean
        SecurityJwtPublisher securityJwtPublisher() {
            return new BaseSecurityJwtPublisher(
                    this.applicationEventPublisher()
            );
        }
    }

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SecurityJwtPublisher componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.applicationEventPublisher
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.applicationEventPublisher
        );
    }

    @Test
    public void publishAuthenticationLoginTest() {
        // Arrange
        var event = entity(EventAuthenticationLogin.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogin(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }

    @Test
    public void publishAuthenticationLoginFailureUsernamePasswordTest() {
        // Arrange
        var event = entity(EventAuthenticationLoginFailureUsernamePassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernamePassword(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }

    @Test
    public void publishAuthenticationLoginFailureUsernameMaskedPasswordTest() {
        // Arrange
        var event = entity(EventAuthenticationLoginFailureUsernameMaskedPassword.class);

        // Act
        this.componentUnderTest.publishAuthenticationLoginFailureUsernameMaskedPassword(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }

    @Test
    public void publishAuthenticationLogoutTest() {
        // Arrange
        var event = entity(EventAuthenticationLogout.class);

        // Act
        this.componentUnderTest.publishAuthenticationLogout(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }

    @Test
    public void publishRegistrationRegister1Test() {
        // Arrange
        var event = entity(EventRegistration1.class);

        // Act
        this.componentUnderTest.publishRegistration1(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }

    @Test
    public void publishSessionRefreshedTest() {
        // Arrange
        var event = entity(EventSessionRefreshed.class);

        // Act
        this.componentUnderTest.publishSessionRefreshed(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }

    @Test
    public void publishSessionExpiredTest() {
        // Arrange
        var event = entity(EventSessionExpired.class);

        // Act
        this.componentUnderTest.publishSessionExpired(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }

    @Test
    public void publishSessionAddUserRequestMetadataTest() {
        // Arrange
        var event = entity(EventSessionAddUserRequestMetadata.class);

        // Act
        this.componentUnderTest.publishSessionAddUserRequestMetadata(event);

        // Assert
        verify(this.applicationEventPublisher).publishEvent(eq(event));
    }
}
