package io.tech1.framework.domain.properties.configs;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertiesConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryPropertiesConfigs;

public abstract class AbstractPropertiesConfigs {

    public void assertProperties(String propertyName) {
        assertMandatoryPropertiesConfigs(this, propertyName);
    }

    public void printProperties(String propertyName) {
        printMandatoryPropertiesConfigs(this, propertyName);
    }
}
