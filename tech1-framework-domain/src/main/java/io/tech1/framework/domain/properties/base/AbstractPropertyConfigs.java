package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.constants.LogsConstants;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printPropertyConfigs;

public abstract class AbstractPropertyConfigs {

    public void assertProperties(String parentName) {
        assertMandatoryPropertyConfigs(this, parentName);
    }

    public void printProperties(String parentName) {
        if (LogsConstants.DEBUG) {
            printPropertyConfigs(this, parentName);
        }
    }
}
