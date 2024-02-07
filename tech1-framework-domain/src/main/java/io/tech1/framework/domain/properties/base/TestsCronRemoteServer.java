package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigsV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;

// TODO [YYL] delete
// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
//@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class TestsCronRemoteServer extends AbstractPropertiesConfigsV2 {
    @MandatoryProperty
    private final Cron cron;
    @MandatoryProperty
    private final RemoteServer remoteServer;
    @MandatoryProperty
    private final String property1;

    public static TestsCronRemoteServer testsHardcoded() {
        return new TestsCronRemoteServer(Cron.testsHardcoded(), RemoteServer.testsHardcoded(), "pizda");
    }
}
