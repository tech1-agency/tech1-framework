package tech1.framework.foundation.domain.triggers;

import tech1.framework.foundation.domain.base.Username;

public interface AbstractTrigger {
    Username getUsername();
    TriggerType getTriggerType();
    String getReadableDetails();
}
