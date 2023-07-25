package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

public record CookieRefreshToken(String value) {
    public JwtRefreshToken getJwtRefreshToken() {
        return new JwtRefreshToken(this.value);
    }
}
