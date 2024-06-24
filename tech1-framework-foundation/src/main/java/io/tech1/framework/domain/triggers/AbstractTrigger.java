package io.tech1.framework.domain.triggers;

import io.tech1.framework.domain.base.Username;

public interface AbstractTrigger {
    Username getUsername();
    TriggerType getTriggerType();
    String getReadableDetails();
}
