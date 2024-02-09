package io.tech1.framework.domain.properties.configs;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertiesConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryPropertiesConfigs;

public abstract class AbstractPropertiesConfigs {
    public abstract boolean isParentPropertiesNode();

    public void assertProperties(String propertyName) {
        assertMandatoryPropertiesConfigs(this, propertyName);
        if (this.isParentPropertiesNode()) {
            printProperties(propertyName);
        }
    }

    public void printProperties(String propertyName) {
        printMandatoryPropertiesConfigs(this, propertyName);
    }
}
