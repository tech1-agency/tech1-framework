package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record Version(@NotNull String value) {

    @JsonCreator
    public static Version of(String value) {
        return new Version(value);
    }

    public static Version random() {
        return of(randomString());
    }

    public static Version unknown() {
        return new Version(UNKNOWN);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
