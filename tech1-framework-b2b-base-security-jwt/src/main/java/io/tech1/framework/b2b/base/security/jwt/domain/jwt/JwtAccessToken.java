package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record JwtAccessToken(@NotNull String value) {

    @JsonCreator
    public static JwtAccessToken of(String value) {
        return new JwtAccessToken(value);
    }

    public static JwtAccessToken random() {
        return new JwtAccessToken(randomString());
    }

    public static JwtAccessToken unknown() {
        return of(UNKNOWN);
    }

    public static JwtAccessToken testsHardcoded() {
        return of("D9F4AF096BEE11C93D84");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
