package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Password;
import lombok.Data;

// Lombok
@Data
public class RequestUserChangePassword1 {
    private final Password newPassword;
    private final Password confirmPassword;
}
