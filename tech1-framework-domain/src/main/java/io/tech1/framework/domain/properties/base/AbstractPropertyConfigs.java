package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.constants.LogsConstants;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printPropertyConfigs;

public abstract class AbstractPropertyConfigs {

    public void assertProperties(String propertyName) {
        assertMandatoryPropertyConfigs(this, propertyName);
    }

    public void printProperties(String propertyName) {
        if (LogsConstants.DEBUG) {
            printPropertyConfigs(this, propertyName);
        }
    }
}
