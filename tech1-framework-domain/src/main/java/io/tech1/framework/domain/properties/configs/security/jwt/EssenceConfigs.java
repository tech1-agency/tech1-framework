package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.DefaultUser;
import io.tech1.framework.domain.properties.base.DefaultUsers;
import io.tech1.framework.domain.properties.base.InvitationCodes;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigsV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.ZoneId;
import java.util.List;
import java.util.Set;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class EssenceConfigs extends AbstractPropertiesConfigsV2 {
    @MandatoryProperty
    private final DefaultUsers defaultUsers;
    @MandatoryProperty
    private final InvitationCodes invitationCodes;

    public static EssenceConfigs testsHardcoded() {
        return new EssenceConfigs(
                new DefaultUsers(
                        true,
                        List.of(
                                new DefaultUser(
                                        Username.of("admin12"),
                                        Password.of("password12"),
                                        ZoneId.systemDefault(),
                                        null,
                                        Set.of("admin")
                                )
                        )
                ),
                InvitationCodes.enabled()
        );
    }

    public static EssenceConfigs random() {
        return new EssenceConfigs(
                DefaultUsers.random(),
                InvitationCodes.random()
        );
    }
}
