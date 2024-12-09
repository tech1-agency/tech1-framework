package jbst.iam.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jbst.foundation.domain.base.Password;

import static jbst.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

public record RequestUserResetPassword(
        @NotNull @NotEmpty String token,
        @Password.ValidPasswordCamelCaseLettersAndNumbers(min = 8, max = 20) Password newPassword,
        @Password.ValidPasswordNotBlank Password confirmPassword
) {

    public static RequestUserResetPassword hardcoded() {
        return new RequestUserResetPassword(
                "V2orWAWX4xlvam9V7u5aUqpgriM6qd8qRsgGyqNw",
                Password.hardcoded(),
                Password.hardcoded()
        );
    }

    public static RequestUserResetPassword random() {
        return new RequestUserResetPassword(
                randomStringLetterOrNumbersOnly(36),
                Password.random(),
                Password.random()
        );
    }

    public void assertPasswordsOrThrow() {
        this.newPassword.assertEqualsOrThrow(this.confirmPassword);
    }
}
