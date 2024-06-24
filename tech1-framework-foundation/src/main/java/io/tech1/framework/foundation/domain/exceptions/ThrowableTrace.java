package io.tech1.framework.foundation.domain.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record ThrowableTrace(String value) {

    @JsonCreator
    public static ThrowableTrace of(String value) {
        return new ThrowableTrace(value);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
