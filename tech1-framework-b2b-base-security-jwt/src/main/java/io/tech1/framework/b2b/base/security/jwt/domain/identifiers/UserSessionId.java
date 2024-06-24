package io.tech1.framework.b2b.base.security.jwt.domain.identifiers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.foundation.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.foundation.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;

public record UserSessionId(@NotNull String value) {

    @JsonCreator
    public static UserSessionId of(String value) {
        return new UserSessionId(value);
    }

    public static UserSessionId undefined() {
        return new UserSessionId(UNDEFINED);
    }

    public static UserSessionId random() {
        return new UserSessionId(randomString());
    }

    public static UserSessionId unknown() {
        return of(UNKNOWN);
    }

    public static UserSessionId testsHardcoded() {
        return of("8DE052C55BD26A1A6F0E");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
