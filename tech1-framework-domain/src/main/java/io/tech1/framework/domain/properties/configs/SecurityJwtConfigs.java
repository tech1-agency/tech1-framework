package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Checkbox;
import io.tech1.framework.domain.properties.base.Mongodb;
import io.tech1.framework.domain.properties.configs.security.jwt.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityJwtConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private AuthoritiesConfigs authoritiesConfigs;
    @MandatoryProperty
    private CookiesConfigs cookiesConfigs;
    @MandatoryProperty
    private EssenceConfigs essenceConfigs;
    @MandatoryProperty
    private JwtTokensConfigs jwtTokensConfigs;
    @MandatoryProperty
    private LoggingConfigs loggingConfigs;
    @MandatoryProperty
    private Mongodb mongodb;
    @MandatoryProperty
    private SessionConfigs sessionConfigs;
    @MandatoryProperty
    private UsersEmailsConfigs usersEmailsConfigs;

    // NOTE: test-purposes
    public static SecurityJwtConfigs of(
            AuthoritiesConfigs authoritiesConfigs,
            CookiesConfigs cookiesConfigs,
            EssenceConfigs essenceConfigs,
            JwtTokensConfigs jwtTokensConfigs,
            LoggingConfigs loggingConfigs,
            Mongodb mongodb,
            SessionConfigs sessionConfigs,
            UsersEmailsConfigs usersEmailsConfigs
    ) {
        var instance = new SecurityJwtConfigs();
        instance.authoritiesConfigs = authoritiesConfigs;
        instance.cookiesConfigs = cookiesConfigs;
        instance.essenceConfigs = essenceConfigs;
        instance.jwtTokensConfigs = jwtTokensConfigs;
        instance.loggingConfigs = loggingConfigs;
        instance.mongodb = mongodb;
        instance.sessionConfigs = sessionConfigs;
        instance.usersEmailsConfigs = usersEmailsConfigs;
        return instance;
    }

    // NOTE: test-purposes
    public static SecurityJwtConfigs disabledUsersEmailsConfigs() {
        var instance = new SecurityJwtConfigs();
        instance.usersEmailsConfigs = UsersEmailsConfigs.of(
                "[Tech1]",
                Checkbox.disabled(),
                Checkbox.disabled()
        );
        return instance;
    }
}
