package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public record Password(@NotNull String value) {

    @JsonCreator
    public static Password of(String value) {
        return new Password(value);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
