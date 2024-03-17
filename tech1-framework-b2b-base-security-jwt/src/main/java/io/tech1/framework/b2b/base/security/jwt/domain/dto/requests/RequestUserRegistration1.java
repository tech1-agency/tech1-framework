package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;

public record RequestUserRegistration1(
        Username username,
        Password password,
        Password confirmPassword,
        String zoneId,
        String invitationCode
) {

    public static RequestUserRegistration1 testsHardcoded() {
        return new RequestUserRegistration1(
                Username.of("registration11"),
                Password.testsHardcoded(),
                Password.testsHardcoded(),
                randomZoneId().getId(),
                randomString()
        );
    }
}
