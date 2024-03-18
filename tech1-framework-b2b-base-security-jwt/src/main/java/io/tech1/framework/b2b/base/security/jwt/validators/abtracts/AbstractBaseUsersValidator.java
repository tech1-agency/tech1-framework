package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersValidator;
import io.tech1.framework.domain.base.Username;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.isEmail;
import static java.util.Objects.nonNull;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseUsersValidator implements BaseUsersValidator {
    // WARNING: Configs vs. Hardcoded?
    protected static final int NEW_PASSWORD_MIN_LENGTH = 8;

    // Repositories
    protected final UsersRepository usersRepository;

    @Override
    public void validateUserUpdateRequest1(Username username, RequestUserUpdate1 request) {
        var zoneId = request.zoneId();
        assertZoneIdOrThrow(zoneId, invalidAttribute("zoneId"));

        var email = request.email();

        var invalidEmailMessage = invalidAttribute("email");
        assertNonNullOrThrow(email, invalidEmailMessage);

        if (!isEmail(email)) {
            throw new IllegalArgumentException(invalidEmailMessage);
        }
        var user = this.usersRepository.findByEmailAsJwtUserOrNull(email);
        // `email` is already used by other user in the system
        if (nonNull(user) && !user.username().equals(username)) {
            throw new IllegalArgumentException(invalidEmailMessage);
        }
    }

    @Override
    public void validateUserUpdateRequest2(RequestUserUpdate2 request) {
        var zoneId = request.zoneId();
        assertZoneIdOrThrow(zoneId, invalidAttribute("zoneId"));
    }

    @Override
    public void validateUserChangePasswordRequestBasic(RequestUserChangePasswordBasic request) {
        var newPassword = request.newPassword();
        var confirmPassword = request.confirmPassword();

        assertNonNullNotBlankOrThrow(newPassword, invalidAttribute("newPassword"));
        assertNonNullNotBlankOrThrow(confirmPassword, invalidAttribute("confirmPassword"));

        newPassword.assertContainsCamelCaseLettersAndNumbersWithLengthOrThrow(NEW_PASSWORD_MIN_LENGTH);

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Provided new password and confirm password are not the same");
        }
    }
}
