package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.constants.LogsConstants;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryPropertyConfigs;

@Slf4j
public abstract class AbstractPropertyConfigs {

    public void assertProperties(String propertyName) {
        assertMandatoryPropertyConfigs(this, propertyName);
    }

    public void printProperties(String propertyName) {
        if (LogsConstants.DEBUG) {
            printMandatoryPropertyConfigs(this, propertyName);
        }
    }
}
