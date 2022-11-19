package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotBlankOrThrow;
import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
public class Version {
    @JsonValue
    private final String value;

    @JsonCreator
    public Version(String value) {
        assertNonNullNotBlankOrThrow(value, invalidAttribute("Version.value"));
        this.value = value;
    }

    public static Version of(String value) {
        return new Version(value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static Version unknown() {
        return new Version(UNKNOWN);
    }
}
