package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record Password(String value) {

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
