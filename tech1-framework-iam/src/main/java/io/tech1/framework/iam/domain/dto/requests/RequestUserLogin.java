package io.tech1.framework.iam.domain.dto.requests;

import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;

public record RequestUserLogin(
        @Username.ValidUsername Username username,
        @Password.ValidPasswordNotBlank Password password
) {
    public static RequestUserLogin testsHardcoded() {
        return new RequestUserLogin(Username.testsHardcoded(), Password.testsHardcoded());
    }

    public static RequestUserLogin random() {
        return new RequestUserLogin(Username.random(), Password.random());
    }
}
