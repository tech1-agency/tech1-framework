package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public record Username(@NotNull String identifier) {

    @JsonCreator
    public static Username of(String identifier) {
        return new Username(identifier);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.identifier;
    }
}
