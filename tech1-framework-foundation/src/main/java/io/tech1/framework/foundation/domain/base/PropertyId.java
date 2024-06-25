package io.tech1.framework.foundation.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.foundation.domain.constants.StringConstants;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;

public record PropertyId(@NotNull String value) {
    @JsonCreator
    public static PropertyId of(String value) {
        return new PropertyId(value);
    }

    public static PropertyId random() {
        return of(randomString());
    }

    public static PropertyId undefined() {
        return of(StringConstants.UNDEFINED);
    }

    public static PropertyId dash() {
        return of(StringConstants.DASH);
    }

    public static PropertyId hyphen() {
        return of(StringConstants.HYPHEN);
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
