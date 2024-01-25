package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record CookieRefreshToken(String value) {
    public static CookieRefreshToken random() {
        return new CookieRefreshToken(randomString());
    }

    public static CookieRefreshToken unknown() {
        return new CookieRefreshToken(UNKNOWN);
    }

    public static CookieRefreshToken testsHardcoded() {
        return new CookieRefreshToken("AE3C542E4368A21EA007");
    }

    public JwtRefreshToken getJwtRefreshToken() {
        return new JwtRefreshToken(this.value);
    }
}
