package jbst.iam.domain.jwt;

import static jbst.foundation.domain.constants.StringConstants.UNKNOWN;
import static jbst.foundation.utilities.random.RandomUtility.randomString;

public record RequestAccessToken(String value) {
    public static RequestAccessToken random() {
        return new RequestAccessToken(randomString());
    }

    @SuppressWarnings("unused")
    public static RequestAccessToken unknown() {
        return new RequestAccessToken(UNKNOWN);
    }

    public static RequestAccessToken testsHardcoded() {
        return new RequestAccessToken("8CF7449A7D1766DE33AD");
    }

    public JwtAccessToken getJwtAccessToken() {
        return new JwtAccessToken(this.value);
    }
}
