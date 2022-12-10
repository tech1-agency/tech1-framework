package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Checkbox;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class UsersEmailsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String subjectPrefix;
    @MandatoryProperty
    private Checkbox authenticationLogin;
    @MandatoryProperty
    private Checkbox sessionRefreshed;

    // NOTE: test-purposes
    public static UsersEmailsConfigs of(
            String subjectPrefix,
            Checkbox authenticationLogin,
            Checkbox sessionRefreshed
    ) {
        var instance = new UsersEmailsConfigs();
        instance.subjectPrefix = subjectPrefix;
        instance.authenticationLogin = authenticationLogin;
        instance.sessionRefreshed = sessionRefreshed;
        return instance;
    }
}
