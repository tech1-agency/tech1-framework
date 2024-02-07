package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigsV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// TODO [YYL] delete
// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
//@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class TestsCronRemoteServer2 extends AbstractPropertiesConfigsV2 {
    @MandatoryProperty
    private final TestsCronRemoteServer testsCronRemoteServer;
    @MandatoryProperty
    private final RemoteServer remoteServer;
    @MandatoryProperty
    private final String attr2;

    public static TestsCronRemoteServer2 testsHardcoded() {
        return new TestsCronRemoteServer2(TestsCronRemoteServer.testsHardcoded(), RemoteServer.testsHardcoded(), "property2");
    }
}
