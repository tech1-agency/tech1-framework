package jbst.foundation.domain.properties.configs;

import jbst.foundation.domain.base.Password;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.annotations.MandatoryToggleProperty;
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
