package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Password;

public record RequestUserChangePassword1(
        Password newPassword,
        Password confirmPassword
) {
}
