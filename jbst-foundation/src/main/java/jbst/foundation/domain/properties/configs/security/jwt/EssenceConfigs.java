package jbst.foundation.domain.properties.configs.security.jwt;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.base.DefaultUsers;
import jbst.foundation.domain.properties.base.InvitationCodes;
import jbst.foundation.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class EssenceConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final DefaultUsers defaultUsers;
    @MandatoryProperty
    private final InvitationCodes invitationCodes;

    public static EssenceConfigs testsHardcoded() {
        return new EssenceConfigs(
                DefaultUsers.testsHardcoded(),
                InvitationCodes.testsHardcoded()
        );
    }

    public static EssenceConfigs random() {
        return new EssenceConfigs(
                DefaultUsers.random(),
                InvitationCodes.random()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return false;
    }
}
