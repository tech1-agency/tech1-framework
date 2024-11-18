package tech1.framework.foundation.domain.properties.configs.security.jwt;

import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import tech1.framework.foundation.domain.properties.base.Checkbox;
import tech1.framework.foundation.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class UsersEmailsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String subjectPrefix;
    @MandatoryProperty
    private final Checkbox authenticationLogin;
    @MandatoryProperty
    private final Checkbox sessionRefreshed;

    public static UsersEmailsConfigs testsHardcoded() {
        return new UsersEmailsConfigs(
                "[Tech1]",
                Checkbox.enabled(),
                Checkbox.enabled()
        );
    }

    public static UsersEmailsConfigs random() {
        return new UsersEmailsConfigs(
                randomString(),
                Checkbox.enabled(),
                Checkbox.enabled()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return false;
    }
}
