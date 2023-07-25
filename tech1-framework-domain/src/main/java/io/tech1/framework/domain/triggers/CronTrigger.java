package io.tech1.framework.domain.triggers;

import com.fasterxml.jackson.annotation.JsonValue;

public record CronTrigger() implements AbstractTrigger {

    @Override
    public String getTriggerType() {
        return "Cron";
    }

    @JsonValue
    @Override
    public String getReadableDetails() {
        return this.getTriggerType();
    }
}
