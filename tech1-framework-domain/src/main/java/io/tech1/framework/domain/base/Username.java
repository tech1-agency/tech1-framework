package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record Username(@NotNull String identifier) {

    @JsonCreator
    public static Username of(String identifier) {
        return new Username(identifier);
    }

    public static Username random() {
        return of(randomString());
    }

    @JsonValue
    @Override
    public String toString() {
        return this.identifier;
    }
}
