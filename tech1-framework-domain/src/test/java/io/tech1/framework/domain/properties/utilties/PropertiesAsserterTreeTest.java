package io.tech1.framework.domain.properties.utilties;

import io.tech1.framework.domain.properties.base.Cron;
import io.tech1.framework.domain.properties.base.RemoteServer;
import org.junit.jupiter.api.Test;

// TODO [YYL] delete
@Deprecated
class PropertiesAsserterTreeTest {

    @Test
    void serverConfigsTest() {
        // Act
        RemoteServer.testsHardcoded().assertProperties("serverName");
        RemoteServer.testsHardcoded().printProperties("serverName");

        // Assert
        // no asserts
    }

    @Test
    void cronTest() {
        // Act
        Cron.enabled().assertProperties("cron");
        Cron.enabled().printProperties("cron");

        // Assert
        // no asserts
    }
}
