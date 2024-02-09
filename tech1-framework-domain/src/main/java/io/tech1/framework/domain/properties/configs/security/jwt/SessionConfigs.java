package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Cron;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class SessionConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final Cron cleanSessionsByExpiredRefreshTokensCron;
    @MandatoryProperty
    private final Cron enableSessionsMetadataRenewCron;

    public static SessionConfigs testsHardcoded() {
        return new SessionConfigs(
                Cron.enabled("*/30 * * * * *", "Europe/Kiev"),
                Cron.enabled("*/15 * * * * *", "Europe/Kiev")
        );
    }

    public static SessionConfigs random() {
        return new SessionConfigs(
                Cron.random(),
                Cron.random()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return false;
    }
}
