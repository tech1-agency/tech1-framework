package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUserValidator;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoBaseUserValidatorTest {

    private static Stream<Arguments> validateUserChangePasswordRequest1Test() {
        return Stream.of(
                Arguments.of(new RequestUserChangePassword1(null, null), invalidAttribute("newPassword")),
                Arguments.of(new RequestUserChangePassword1(Password.of("simple"), null), invalidAttribute("confirmPassword")),
                Arguments.of(new RequestUserChangePassword1(Password.of("simple"), Password.of(randomString())), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePassword1(Password.of("Simple"), Password.of(randomString())), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePassword1(Password.of("Simple1"), Password.of(randomString())), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePassword1(Password.of("ComPLEx12"), Password.of("NoMatch")), "Confirm password should match new password"),
                Arguments.of(new RequestUserChangePassword1(Password.of("ComPLEx12"), Password.of("ComPLEx12")), null)
        );
    }

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    static class ContextConfiguration {

    }

    private final MongoUsersRepository mongoUsersRepository;

    private final BaseUserValidator componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.mongoUsersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.mongoUsersRepository
        );
    }

    @Test
    void validateUserUpdateRequest1InvalidZoneIdTest() {
        // Arrange
        var username = entity(Username.class);
        var requestUserUpdate1 = new RequestUserUpdate1("invalidZoneId", randomEmail(), randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `zoneId` is invalid");
    }

    @Test
    void validateUserUpdateRequest1InvalidEmailTest() {
        // Arrange
        var username = entity(Username.class);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), Email.of(randomString()), randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `email` is invalid");
    }

    @Test
    void validateUserUpdateRequest1EmailValidNoUserTest() {
        // Arrange
        var username = entity(Username.class);
        var email = randomEmail();
        when(this.mongoUsersRepository.findByEmail(email)).thenReturn(null);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable).isNull();
        verify(this.mongoUsersRepository).findByEmail(email);
    }

    @Test
    void validateUserUpdateRequest1EmailValidUserFoundTest() {
        // Arrange
        var user= entity(MongoDbUser.class);
        var email = randomEmail();
        when(this.mongoUsersRepository.findByEmail(email)).thenReturn(user);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(user.getUsername(), requestUserUpdate1));

        // Assert
        assertThat(throwable).isNull();
        verify(this.mongoUsersRepository).findByEmail(email);
    }

    @Test
    void validateUserUpdateRequest1EmailValidTwoUsersTest() {
        // Arrange
        var username = entity(Username.class);
        var email = randomEmail();
        var user = entity(MongoDbUser.class);
        when(this.mongoUsersRepository.findByEmail(email)).thenReturn(user);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(username, requestUserUpdate1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `email` is invalid");
        verify(this.mongoUsersRepository).findByEmail(email);
    }

    @Test
    void validateUserUpdateRequest2InvalidZoneIdTest() {
        // Arrange
        var requestUserUpdate2 = new RequestUserUpdate2("invalidZoneId", randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest2(requestUserUpdate2));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attribute `zoneId` is invalid");
    }

    @Test
    void validateUserUpdateRequest2Test() {
        // Arrange
        var requestUserUpdate2 = new RequestUserUpdate2(randomZoneId().getId(), randomString());

        // Act
        this.componentUnderTest.validateUserUpdateRequest2(requestUserUpdate2);

        // Assert
        // no asserts
    }

    @ParameterizedTest
    @MethodSource("validateUserChangePasswordRequest1Test")
    void validateUserChangePasswordRequest1Test(RequestUserChangePassword1 requestUserChangePassword1, String exceptionMessage) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserChangePasswordRequest1(requestUserChangePassword1));

        // Assert
        if (nonNull(exceptionMessage)) {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(exceptionMessage);
        } else {
            assertThat(throwable).isNull();
        }
    }

}
