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
public class Username {

    @JsonValue
    private final String identifier;

    @JsonCreator
    public Username(String identifier) {
        assertNonNullNotBlankOrThrow(identifier, invalidAttribute("Username.identifier"));
        this.identifier = identifier;
    }

    public static Username of(String identifier) {
        return new Username(identifier);
    }

    @Override
    public String toString() {
        return this.identifier;
    }
}
