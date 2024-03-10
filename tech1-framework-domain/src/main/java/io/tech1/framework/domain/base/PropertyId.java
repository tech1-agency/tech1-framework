package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record PropertyId(@NotNull String value) {
    @JsonCreator
    public static PropertyId of(String value) {
        return new PropertyId(value);
    }

    public static PropertyId random() {
        return of(randomString());
    }

    public static PropertyId undefined() {
        return of(UNDEFINED);
    }

    public static PropertyId testsHardcoded() {
        return of("A0814EF707DAF2FDE2D4");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
