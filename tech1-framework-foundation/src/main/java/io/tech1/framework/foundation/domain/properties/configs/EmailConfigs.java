package io.tech1.framework.foundation.domain.properties.configs;

import io.tech1.framework.foundation.domain.base.Password;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.foundation.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailConfigs extends AbstractTogglePropertiesConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @NonMandatoryProperty
    private String host;
    @NonMandatoryProperty
    private Integer port;
    @NonMandatoryProperty
    private String from;
    @NonMandatoryProperty
    private Username username;
    @NonMandatoryProperty
    private Password password;

    public static EmailConfigs testsHardcoded() {
        return new EmailConfigs(
                true,
                "smtp.gmail.com",
                587,
                "Tech1",
                Username.testsHardcoded(),
                Password.testsHardcoded()
        );
    }

    public static EmailConfigs disabled() {
        return new EmailConfigs(false, null, 0, null, null, null);
    }

    public static EmailConfigs enabled(String from) {
        return new EmailConfigs(true, "smtp.gmail.com", 587, from, Username.testsHardcoded(), Password.testsHardcoded());
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }
}
