package io.tech1.framework.domain.triggers;

import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class UserTrigger implements AbstractTrigger {
    private final Username username;

    public UserTrigger(
            Username username
    ) {
        assertNonNullOrThrow(username, invalidAttribute("UserTrigger.username"));
        this.username = username;
    }

    @Override
    public String getTriggerType() {
        return "User";
    }

    @JsonValue
    @Override
    public String getReadableDetails() {
        return this.getTriggerType() + " — " + this.username;
    }
}
