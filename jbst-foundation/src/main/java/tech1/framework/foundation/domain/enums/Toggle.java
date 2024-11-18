package tech1.framework.foundation.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Lombok
@AllArgsConstructor
@Getter
public enum Toggle {
    ENABLED("Enabled", "enabled"),
    DISABLED("Disabled", "disabled");

    private final String value;
    private final String lowerCase;

    public static Toggle of(boolean toggle) {
        if (toggle) {
            return ENABLED;
        } else {
            return DISABLED;
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    public boolean isEnabled() {
        return ENABLED.equals(this);
    }

    public boolean isDisabled() {
        return DISABLED.equals(this);
    }
}
