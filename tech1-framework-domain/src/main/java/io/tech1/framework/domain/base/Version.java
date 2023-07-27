package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;

public record Version(@NotNull String value) {

    @JsonCreator
    public static Version of(String value) {
        return new Version(value);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    public static Version unknown() {
        return new Version(UNKNOWN);
    }
}
