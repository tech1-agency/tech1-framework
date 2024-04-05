package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.ZoneId;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;

public record RequestUserRegistration1(
        @Username.ValidUsername Username username,
        @Password.ValidPasswordNotBlank Password password,
        @Password.ValidPasswordNotBlank Password confirmPassword,
        @NotNull ZoneId zoneId,
        @NotBlank String invitationCode
) {

    public static RequestUserRegistration1 testsHardcoded() {
        return new RequestUserRegistration1(
                Username.of("registration11"),
                Password.testsHardcoded(),
                Password.testsHardcoded(),
                randomZoneId(),
                randomString()
        );
    }

    public void assertPasswordsOrThrow() {
        this.password.assertEqualsOrThrow(this.confirmPassword);
    }
}
