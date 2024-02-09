package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomIPv4;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class RemoteServer extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final String baseURL;
    @MandatoryProperty
    private final Username username;
    @MandatoryProperty
    private final Password password;

    public static RemoteServer testsHardcoded() {
        return new RemoteServer("localhost", Username.testsHardcoded(), Password.testsHardcoded());
    }

    public static RemoteServer random() {
        return new RemoteServer(randomIPv4(), Username.random(), Password.random());
    }
}
