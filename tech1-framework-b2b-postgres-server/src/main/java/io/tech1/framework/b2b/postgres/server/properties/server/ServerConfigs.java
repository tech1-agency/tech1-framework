package io.tech1.framework.b2b.postgres.server.properties.server;

import io.tech1.framework.b2b.postgres.server.domain.enums.UserAuthority;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String targetAttribute1;
    @MandatoryProperty
    private long targetAttribute2;
    @MandatoryProperty
    private UserAuthority targetAuthority;

    // NOTE: test-purposes
    public static ServerConfigs of(
            String targetAttribute1,
            long targetAttribute2,
            UserAuthority targetAuthority
    ) {
        var instance = new ServerConfigs();
        instance.targetAttribute1 = targetAttribute1;
        instance.targetAttribute2 = targetAttribute2;
        instance.targetAuthority = targetAuthority;
        return instance;
    }
}
