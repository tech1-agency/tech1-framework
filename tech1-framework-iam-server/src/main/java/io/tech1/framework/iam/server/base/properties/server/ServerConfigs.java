package io.tech1.framework.iam.server.base.properties.server;

import io.tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.foundation.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.iam.server.base.domain.enums.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerConfigs extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final String targetAttribute1;
    @MandatoryProperty
    private final long targetAttribute2;
    @MandatoryProperty
    private final UserAuthority targetAuthority;
}
