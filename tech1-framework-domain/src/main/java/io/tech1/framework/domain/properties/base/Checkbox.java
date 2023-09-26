package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
public class Checkbox implements AbstractToggleProperty {
    @MandatoryProperty
    private final boolean enabled;

    public static Checkbox enabled() {
        return new Checkbox(true);
    }

    public static Checkbox disabled() {
        return new Checkbox(false);
    }
}
