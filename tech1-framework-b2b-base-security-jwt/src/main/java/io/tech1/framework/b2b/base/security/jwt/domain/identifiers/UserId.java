package io.tech1.framework.b2b.base.security.jwt.domain.identifiers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.domain.base.Username;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record UserId(@NotNull String value) {

    @JsonCreator
    public static UserId of(String value) {
        return new UserId(value);
    }

    public static UserId random() {
        return new UserId(randomString());
    }

    public static UserId unknown() {
        return of(UNKNOWN);
    }

    public static UserId testsHardcoded() {
        return of("72667893848372913475");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
