package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public record Email(@NotNull String value) {

    @JsonCreator
    public static Email of(String value) {
        return new Email(value);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
