package io.tech1.framework.domain.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotBlankOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
public class ThrowableTrace {
    @JsonValue
    private final String value;

    @JsonCreator
    public ThrowableTrace(String value) {
        assertNonNullNotBlankOrThrow(value, invalidAttribute("ThrowableTrace.value"));
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
