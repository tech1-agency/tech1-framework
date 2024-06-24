package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.base.PropertyId;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertiesConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryPropertiesConfigs;

public abstract class AbstractPropertiesConfigs {
    public abstract boolean isParentPropertiesNode();

    public void assertProperties(PropertyId propertyId) {
        assertMandatoryPropertiesConfigs(this, propertyId);
        if (this.isParentPropertiesNode()) {
            printProperties(propertyId);
        }
    }

    public void printProperties(PropertyId propertyId) {
        printMandatoryPropertiesConfigs(this, propertyId);
    }
}
