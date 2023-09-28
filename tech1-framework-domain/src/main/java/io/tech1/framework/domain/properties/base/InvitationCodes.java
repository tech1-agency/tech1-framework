package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
public class InvitationCodes implements AbstractToggleProperty {
    @MandatoryProperty
    private final boolean enabled;

    public static InvitationCodes enabled() {
        return new InvitationCodes(true);
    }

    public static InvitationCodes disabled() {
        return new InvitationCodes(false);
    }
}
