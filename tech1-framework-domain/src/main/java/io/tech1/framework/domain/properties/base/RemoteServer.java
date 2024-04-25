package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.UsernamePasswordCredentials;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomIPv4;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class RemoteServer extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final String baseURL;
    @MandatoryProperty
    private final UsernamePasswordCredentials credentials;

    public static RemoteServer testsHardcoded() {
        return new RemoteServer("localhost", UsernamePasswordCredentials.testsHardcoded());
    }

    public static RemoteServer random() {
        return new RemoteServer(randomIPv4(), UsernamePasswordCredentials.testsHardcoded());
    }

    public boolean containsCredentials(@NotNull UsernamePasswordCredentials credentials) {
        return this.credentials.equals(credentials);
    }
}
