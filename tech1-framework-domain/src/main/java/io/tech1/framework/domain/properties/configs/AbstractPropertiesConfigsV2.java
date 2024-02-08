package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.constants.LogsConstants;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertiesConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printPropertiesConfigs;

public abstract class AbstractPropertiesConfigsV2 {

    public void assertProperties(String propertyName) {
        assertMandatoryPropertiesConfigs(this, propertyName);
    }

    public void printProperties(String propertyName) {
        if (LogsConstants.DEBUG) {
            printPropertiesConfigs(this, propertyName);
        }
    }
}
