package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record CookieAccessToken(String value) {
    public static CookieAccessToken random() {
        return new CookieAccessToken(randomString());
    }

    public JwtAccessToken getJwtAccessToken() {
        return new JwtAccessToken(this.value);
    }
}
