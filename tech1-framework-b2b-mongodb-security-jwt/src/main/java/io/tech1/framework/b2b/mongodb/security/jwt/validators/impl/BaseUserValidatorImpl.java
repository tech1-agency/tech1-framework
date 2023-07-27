package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.BaseUserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.containsCamelCaseLettersAndNumbersWithLength;
import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.isEmail;
import static java.util.Objects.nonNull;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseUserValidatorImpl implements BaseUserValidator {
    private static final int NEW_PASSWORD_MIN_LENGTH = 8;

    // Repositories
    private final UserRepository userRepository;

    @Override
    public void validateUserUpdateRequest1(DbUser currentDbUser, RequestUserUpdate1 requestUserUpdate1) {
        var zoneId = requestUserUpdate1.zoneId();
        assertZoneIdOrThrow(zoneId, invalidAttribute("zoneId"));

        var email = requestUserUpdate1.email();

        var invalidEmailMessage = invalidAttribute("email");
        assertNonNullOrThrow(email, invalidEmailMessage);

        if (!isEmail(email)) {
            throw new IllegalArgumentException(invalidEmailMessage);
        }
        var user = this.userRepository.findByEmail(email);
        // `email` is already used by other user in the system
        if (nonNull(user) && !user.getUsername().equals(currentDbUser.getUsername())) {
            throw new IllegalArgumentException(invalidEmailMessage);
        }
    }

    @Override
    public void validateUserUpdateRequest2(RequestUserUpdate2 requestUserUpdate2) {
        var zoneId = requestUserUpdate2.zoneId();
        assertZoneIdOrThrow(zoneId, invalidAttribute("zoneId"));
    }

    @Override
    public void validateUserChangePasswordRequest1(RequestUserChangePassword1 requestUserChangePassword1) {
        var newPassword = requestUserChangePassword1.newPassword();
        var confirmPassword = requestUserChangePassword1.confirmPassword();

        assertNonNullNotBlankOrThrow(newPassword, invalidAttribute("newPassword"));
        assertNonNullNotBlankOrThrow(confirmPassword, invalidAttribute("confirmPassword"));

        if (!containsCamelCaseLettersAndNumbersWithLength(newPassword, NEW_PASSWORD_MIN_LENGTH)) {
            var message = "New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least " + NEW_PASSWORD_MIN_LENGTH + " characters long";
            throw new IllegalArgumentException(message);
        }
        if (!newPassword.equals(confirmPassword)) {
            var message = "Confirm password should match new password";
            throw new IllegalArgumentException(message);
        }
    }
}
