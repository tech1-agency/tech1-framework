package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Password;

public record RequestUserChangePasswordBasic(
        @Password.ValidPasswordCamelCaseLettersAndNumbers(min = 8, max = 20) Password newPassword,
        @Password.ValidPasswordNotBlank Password confirmPassword
) {

    public static RequestUserChangePasswordBasic testsHardcoded() {
        return new RequestUserChangePasswordBasic(
                Password.testsHardcoded(),
                Password.testsHardcoded()
        );
    }

    public static RequestUserChangePasswordBasic random() {
        return new RequestUserChangePasswordBasic(
                Password.random(),
                Password.random()
        );
    }

    public void assertPasswordsOrThrow() {
        this.newPassword.assertEqualsOrThrow(this.confirmPassword);
    }
}
