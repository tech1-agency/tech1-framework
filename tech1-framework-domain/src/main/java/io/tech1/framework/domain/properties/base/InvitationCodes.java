package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class InvitationCodes implements AbstractToggleProperty {
    @MandatoryProperty
    private boolean enabled;

    // NOTE: test-purposes
    public static InvitationCodes of(
            boolean enabled
    ) {
        var instance = new InvitationCodes();
        instance.enabled = enabled;
        return instance;
    }
}
