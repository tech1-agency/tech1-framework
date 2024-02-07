package io.tech1.framework.domain.properties.utilties;

import io.tech1.framework.domain.properties.base.RemoteServer;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printPropertyConfigs;

class PropertiesAsserterTreeTest {

    @Test
    void serverConfigsTest() {
        // Act
        assertMandatoryPropertyConfigs(RemoteServer.testsHardcoded(), "remoteServer");
        printPropertyConfigs(RemoteServer.testsHardcoded(), "remoteServer");

        // Assert
        // no asserts
    }
}
