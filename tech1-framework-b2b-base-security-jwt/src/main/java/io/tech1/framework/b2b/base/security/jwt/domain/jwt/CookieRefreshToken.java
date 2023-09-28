package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record CookieRefreshToken(String value) {
    public static CookieRefreshToken random() {
        return new CookieRefreshToken(randomString());
    }

    public JwtRefreshToken getJwtRefreshToken() {
        return new JwtRefreshToken(this.value);
    }
}
