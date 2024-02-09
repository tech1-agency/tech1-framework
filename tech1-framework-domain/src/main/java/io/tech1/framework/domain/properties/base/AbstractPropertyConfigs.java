package io.tech1.framework.domain.properties.base;

import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryBasedConfigs;

@Slf4j
public abstract class AbstractPropertyConfigs {

    public void assertProperties(String propertyName) {
        assertMandatoryPropertyConfigs(this, propertyName);
    }

    public void printProperties(String propertyName) {
        printMandatoryBasedConfigs(this, propertyName);
    }
}
