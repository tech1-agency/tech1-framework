package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

@Deprecated
public record CookieAccessToken(String value) {
    public static CookieAccessToken random() {
        return new CookieAccessToken(randomString());
    }

    public static CookieAccessToken unknown() {
        return new CookieAccessToken(UNKNOWN);
    }

    public static CookieAccessToken testsHardcoded() {
        return new CookieAccessToken("8CF7449A7D1766DE33AD");
    }

    public JwtAccessToken getJwtAccessToken() {
        return new JwtAccessToken(this.value);
    }
}
