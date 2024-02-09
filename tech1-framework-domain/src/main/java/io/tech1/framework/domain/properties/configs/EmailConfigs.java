package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.MandatoryToggleProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailConfigs extends AbstractTogglePropertiesConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryToggleProperty
    private String host;
    @MandatoryToggleProperty
    private Integer port;
    @MandatoryToggleProperty
    private String from;
    @MandatoryToggleProperty
    private Username username;
    @MandatoryToggleProperty
    private Password password;
    @MandatoryToggleProperty
    private String[] to;

    public static EmailConfigs testsHardcoded() {
        return new EmailConfigs(
                true,
                "smtp.gmail.com",
                587,
                "Tech1",
                Username.testsHardcoded(),
                Password.testsHardcoded(),
                new String[] { Email.random().value(), Email.random().value() }
        );
    }

    public static EmailConfigs disabled() {
        return new EmailConfigs(false, null, 0, null, null, null, null);
    }

    public static EmailConfigs enabled(String from) {
        return new EmailConfigs(true, "smtp.gmail.com", 587, from, Username.testsHardcoded(), Password.testsHardcoded(), new String[] {});
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }
}
