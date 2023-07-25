package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record Email(String value) {

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
