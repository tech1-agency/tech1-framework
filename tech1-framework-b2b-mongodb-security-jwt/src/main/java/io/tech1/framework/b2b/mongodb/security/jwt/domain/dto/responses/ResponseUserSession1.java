package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import lombok.Data;

// Lombok
@Data
public class ResponseUserSession1 {
    private final String refreshToken;
}
