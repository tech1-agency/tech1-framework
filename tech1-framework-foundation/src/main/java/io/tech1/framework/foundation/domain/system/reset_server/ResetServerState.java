package io.tech1.framework.foundation.domain.system.reset_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

// Lombok
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResetServerState {
    READY("Ready"),
    RESETTING("Resetting");

    private final String value;

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonIgnore
    public boolean isResetting() {
        return RESETTING.equals(this);
    }
}
