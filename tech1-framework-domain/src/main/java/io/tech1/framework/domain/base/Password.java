package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotBlankOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
public class Password {
    @JsonValue
    private final String value;

    @JsonCreator
    public Password(String value) {
        assertNonNullNotBlankOrThrow(value, invalidAttribute("Password.value"));
        this.value = value;
    }

    public static Password of(String value) {
        return new Password(value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
