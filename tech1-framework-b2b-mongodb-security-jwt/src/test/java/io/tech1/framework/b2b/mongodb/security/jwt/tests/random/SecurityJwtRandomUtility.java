package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.jsonwebtoken.impl.DefaultClaims;
import lombok.experimental.UtilityClass;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

@UtilityClass
public class SecurityJwtRandomUtility {

    public static DefaultClaims randomValidDefaultClaims() {
        var defaultClaims = new DefaultClaims();
        defaultClaims.setSubject(randomString());
        return defaultClaims;
    }
}
