package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;

public record RequestUserLogin(
        Username username,
        Password password
) {
    public static RequestUserLogin testsHardcoded() {
        return new RequestUserLogin(Username.testsHardcoded(), Password.testsHardcoded());
    }

    public static RequestUserLogin random() {
        return new RequestUserLogin(Username.random(), Password.random());
    }
}
