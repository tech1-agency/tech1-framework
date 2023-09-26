package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Cron;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// TODO [yy] -> properties V2.0
// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class SessionConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private Cron cleanSessionsByExpiredRefreshTokensCron;
    @MandatoryProperty
    private Cron enableSessionsMetadataRenewCron;

    // NOTE: test-purposes
    public static SessionConfigs of(
            Cron cleanSessionsByExpiredRefreshTokensCron,
            Cron enableSessionsMetadataRenewCron
    ) {
        var instance = new SessionConfigs();
        instance.cleanSessionsByExpiredRefreshTokensCron = cleanSessionsByExpiredRefreshTokensCron;
        instance.enableSessionsMetadataRenewCron = enableSessionsMetadataRenewCron;
        return instance;
    }
}
