package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

import lombok.Data;

// Lombok
@Data
public class CookieAccessToken {
    private final String value;

    public JwtAccessToken getJwtAccessToken() {
        return new JwtAccessToken(this.value);
    }
}
