package io.tech1.framework.domain.triggers;

import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.domain.base.Username;

public record UserTrigger(Username username) implements AbstractTrigger {

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
