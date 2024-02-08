package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Mongodb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class MongodbSecurityJwtConfigs extends AbstractPropertiesConfigsV2 {
    @MandatoryProperty
    private final Mongodb mongodb;

    public static MongodbSecurityJwtConfigs testsHardcoded() {
        return new MongodbSecurityJwtConfigs(
                Mongodb.testsHardcoded()
        );
    }

    public static MongodbSecurityJwtConfigs random() {
        return new MongodbSecurityJwtConfigs(
                Mongodb.random()
        );
    }
}
