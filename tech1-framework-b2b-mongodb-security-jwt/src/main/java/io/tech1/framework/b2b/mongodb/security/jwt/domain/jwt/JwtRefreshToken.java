package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

import lombok.Data;

// Lombok
@Data
public class JwtRefreshToken {
    private final String value;
}
