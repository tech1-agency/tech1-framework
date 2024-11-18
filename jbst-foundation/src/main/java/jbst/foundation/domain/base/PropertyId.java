package jbst.foundation.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jbst.foundation.domain.constants.StringConstants;
import org.jetbrains.annotations.NotNull;

import static jbst.foundation.utilities.random.RandomUtility.randomString;

public record PropertyId(@NotNull String value) {
    @JsonCreator
    public static PropertyId of(String value) {
        return new PropertyId(value);
    }

    public static PropertyId hardcoded() {
        return of("A0814EF707DAF2FDE2D4");
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

    @SuppressWarnings("unused")
    public static PropertyId hyphen() {
        return of(StringConstants.HYPHEN);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
