package io.tech1.framework.domain.http.requests;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class UserAgentHeader {
    private final String value;

    public UserAgentHeader(
            String value
    ) {
        assertNonNullOrThrow(value, invalidAttribute("UserAgentDetails.value"));
        this.value = value;
    }
}
