package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEmailAsValue;

public record Email(@NotNull String value) {

    @JsonCreator
    public static Email of(String value) {
        return new Email(value);
    }

    public static Email random() {
        return of(randomEmailAsValue());
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
