package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.Data;

// Lombok
@Data
public class RequestUserChangePassword1 {
    private final String newPassword;
    private final String confirmPassword;
}
