package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.ValidatorsContext;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.BaseUserValidator;
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
public class BaseUserValidatorImplTest {

    private static Stream<Arguments> validateUserChangePasswordRequest1Test() {
        return Stream.of(
                Arguments.of(new RequestUserChangePassword1("", ""), invalidAttribute("newPassword")),
                Arguments.of(new RequestUserChangePassword1("simple", ""), invalidAttribute("confirmPassword")),
                Arguments.of(new RequestUserChangePassword1("simple", randomString()), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePassword1("Simple", randomString()), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePassword1("Simple1", randomString()), "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least 8 characters long"),
                Arguments.of(new RequestUserChangePassword1("ComPLEx12", "NoMatch"), "Confirm password should match new password"),
                Arguments.of(new RequestUserChangePassword1("ComPLEx12", "ComPLEx12"), null)
        );
    }

    @Configuration
    @Import({
            ValidatorsContext.class
    })
    static class ContextConfiguration {


    }

    private final UserRepository userRepository;

    private final BaseUserValidator componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.userRepository
        );
    }

    @AfterEach
    public void after() {
        verifyNoMoreInteractions(
                this.userRepository
        );
    }

    @Test
    public void validateUserUpdateRequest1InvalidZoneIdTest() {
        // Arrange
        var currentDbUser = entity(DbUser.class);
        var requestUserUpdate1 = new RequestUserUpdate1("invalidZoneId", randomString(), randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(currentDbUser, requestUserUpdate1));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Attribute `zoneId` is invalid");
    }

    @Test
    public void validateUserUpdateRequest1InvalidEmailTest() {
        // Arrange
        var currentDbUser = entity(DbUser.class);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), randomString(), randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(currentDbUser, requestUserUpdate1));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Attribute `email` is invalid");
    }

    @Test
    public void validateUserUpdateRequest1EmailValidNoUserTest() {
        // Arrange
        var email = randomEmail();
        var currentDbUser = entity(DbUser.class);
        when(this.userRepository.findByEmail(email)).thenReturn(null);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(currentDbUser, requestUserUpdate1));

        // Assert
        assertThat(throwable).isNull();
        verify(this.userRepository).findByEmail(eq(email));
    }

    @Test
    public void validateUserUpdateRequest1EmailValidUserFoundTest() {
        // Arrange
        var email = randomEmail();
        var currentDbUser = entity(DbUser.class);
        when(this.userRepository.findByEmail(email)).thenReturn(currentDbUser);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(currentDbUser, requestUserUpdate1));

        // Assert
        assertThat(throwable).isNull();
        verify(this.userRepository).findByEmail(eq(email));
    }

    @Test
    public void validateUserUpdateRequest1EmailValidTwoUsersTest() {
        // Arrange
        var email = randomEmail();
        var currentDbUser = entity(DbUser.class);
        var user = entity(DbUser.class);
        when(this.userRepository.findByEmail(email)).thenReturn(user);
        var requestUserUpdate1 = new RequestUserUpdate1(randomZoneId().getId(), email, randomString());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateUserUpdateRequest1(currentDbUser, requestUserUpdate1));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).isEqualTo("Attribute `email` is invalid");
        verify(this.userRepository).findByEmail(eq(email));
    }

    @ParameterizedTest
    @MethodSource("validateUserChangePasswordRequest1Test")
    public void validateUserChangePasswordRequest1Test(RequestUserChangePassword1 requestUserChangePassword1, String exceptionMessage) {
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
