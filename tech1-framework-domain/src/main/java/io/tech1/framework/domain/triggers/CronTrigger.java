package io.tech1.framework.domain.triggers;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Lombok
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CronTrigger implements AbstractTrigger {

    public static CronTrigger of() {
        return new CronTrigger();
    }

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
