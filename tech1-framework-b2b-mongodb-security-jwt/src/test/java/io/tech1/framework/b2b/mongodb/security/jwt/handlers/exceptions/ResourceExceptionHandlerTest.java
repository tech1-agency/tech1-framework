package io.tech1.framework.b2b.mongodb.security.jwt.handlers.exceptions;

import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.HandlersContext;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static io.tech1.framework.domain.exceptions.ExceptionEntityType.ERROR;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceExceptionHandlerTest {

    @Configuration
    @Import({
            HandlersContext.class
    })
    static class ContextConfiguration {

    }

    private final ResourceExceptionHandler componentUnderTest;

    private static Stream<Arguments> unauthorizedResponseErrorMessageTest() {
        return Stream.of(
                Arguments.of(new CookieNotFoundException(randomString())),
                Arguments.of(new CookieAccessTokenNotFoundException()),
                Arguments.of(new CookieAccessTokenInvalidException()),
                Arguments.of(new CookieAccessTokenExpiredException(randomUsername())),
                Arguments.of(new CookieRefreshTokenNotFoundException()),
                Arguments.of( new CookieRefreshTokenInvalidException()),
                Arguments.of(new CookieRefreshTokenExpiredException(randomUsername())),
                Arguments.of(new CookieRefreshTokenDbNotFoundException(randomUsername())),
                Arguments.of(new CookieUnauthorizedException(randomString()))
        );
    }

    @ParameterizedTest
    @MethodSource("unauthorizedResponseErrorMessageTest")
    public void unauthorizedResponseErrorMessageTest(Exception exception) {
        // Act
        var response = this.componentUnderTest.cookiesUnauthorizedExceptions(exception);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getExceptionEntityType()).isEqualTo(ERROR);
        assertThat(response.getBody().getAttributes().get("shortMessage")).isEqualTo(exception.getMessage());
        assertThat(response.getBody().getAttributes().get("fullMessage")).isEqualTo(exception.getMessage());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void accessDeniedExceptionTest() {
        // Arrange
        var message = randomString();
        var exception = new AccessDeniedException(message);

        // Act
        var response = this.componentUnderTest.accessDeniedException(exception);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getExceptionEntityType()).isEqualTo(ERROR);
        assertThat(response.getBody().getAttributes().get("shortMessage")).isEqualTo(exception.getMessage());
        assertThat(response.getBody().getAttributes().get("fullMessage")).isEqualTo(exception.getMessage());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void registrationExceptionTest() {
        // Arrange
        var message = randomString();
        var exception = new RegistrationException(message);

        // Act
        var response = this.componentUnderTest.registerException(exception);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getExceptionEntityType()).isEqualTo(ERROR);
        assertThat(response.getBody().getAttributes().get("shortMessage")).isEqualTo(contactDevelopmentTeam("Registration Failure"));
        assertThat(response.getBody().getAttributes().get("fullMessage")).isEqualTo(exception.getMessage());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void internalServerErrorTest() {
        // Arrange
        var message = randomString();
        var exception = new IllegalArgumentException(message);

        // Act
        var response = this.componentUnderTest.internalServerError(exception);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getExceptionEntityType()).isEqualTo(ERROR);
        assertThat(response.getBody().getAttributes().get("shortMessage")).isEqualTo(exception.getMessage());
        assertThat(response.getBody().getAttributes().get("fullMessage")).isEqualTo(exception.getMessage());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
