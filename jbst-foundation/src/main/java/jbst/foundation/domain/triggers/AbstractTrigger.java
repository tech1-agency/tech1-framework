package jbst.foundation.domain.triggers;

import jbst.foundation.domain.base.Username;

public interface AbstractTrigger {
    Username getUsername();
    TriggerType getTriggerType();
    String getReadableDetails();
}
