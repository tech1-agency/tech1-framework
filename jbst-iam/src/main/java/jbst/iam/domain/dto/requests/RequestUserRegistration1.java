package jbst.iam.domain.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;

import java.time.ZoneId;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomZoneId;
import static tech1.framework.foundation.utilities.zones.ZonesUtility.reworkUkraineZoneId;

public record RequestUserRegistration1(
        @Username.ValidUsername Username username,
        @Password.ValidPasswordCamelCaseLettersAndNumbers(min = 8, max = 20) Password password,
        @Password.ValidPasswordNotBlank Password confirmPassword,
        @Schema(type = "string") @NotNull ZoneId zoneId,
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

    public RequestUserRegistration1 createReworkedUkraineZoneId() {
        return new RequestUserRegistration1(
                this.username,
                this.password,
                this.confirmPassword,
                reworkUkraineZoneId(this.zoneId),
                this.invitationCode
        );
    }
}
