package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class Authority extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final String value;

    public static Authority testsHardcoded() {
        return new Authority("user");
    }

    public static Authority random() {
        return new Authority(randomString());
    }

    @Override
    public String toString() {
        return this.value;
    }
}
