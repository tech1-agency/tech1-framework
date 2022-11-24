package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

import lombok.Data;

// Lombok
@Data
public class CookieRefreshToken {
    private final String value;

    public JwtRefreshToken getJwtRefreshToken() {
        return new JwtRefreshToken(this.value);
    }
}
