package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Cron;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class SessionConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private Cron cleanSessionsByExpiredRefreshTokensCron;

    // NOTE: test-purposes
    public static SessionConfigs of(
            Cron cleanSessionsByExpiredRefreshTokensCron
    ) {
        var instance = new SessionConfigs();
        instance.cleanSessionsByExpiredRefreshTokensCron = cleanSessionsByExpiredRefreshTokensCron;
        return instance;
    }
}
