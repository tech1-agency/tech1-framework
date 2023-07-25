package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

public record CookieAccessToken(String value) {
    public JwtAccessToken getJwtAccessToken() {
        return new JwtAccessToken(this.value);
    }
}
