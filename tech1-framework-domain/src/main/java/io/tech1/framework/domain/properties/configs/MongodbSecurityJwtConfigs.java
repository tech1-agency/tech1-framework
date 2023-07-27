package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Mongodb;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class MongodbSecurityJwtConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private Mongodb mongodb;

    // NOTE: test-purposes
    public static MongodbSecurityJwtConfigs of(
            Mongodb mongodb
    ) {
        var instance = new MongodbSecurityJwtConfigs();
        instance.mongodb = mongodb;
        return instance;
    }
}
