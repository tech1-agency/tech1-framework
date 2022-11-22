package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.DefaultUsers;
import io.tech1.framework.domain.properties.base.InvitationCodes;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class EssenceConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private DefaultUsers defaultUsers;
    @MandatoryProperty
    private InvitationCodes invitationCodes;

    // NOTE: test-purposes
    public static EssenceConfigs of(
            DefaultUsers defaultUsers,
            InvitationCodes invitationCodes
    ) {
        var instance = new EssenceConfigs();
        instance.defaultUsers = defaultUsers;
        instance.invitationCodes = invitationCodes;
        return instance;
    }
}
