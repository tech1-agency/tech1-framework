package io.tech1.framework.foundation.domain.properties.configs;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.utilities.PropertiesAsserter;
import io.tech1.framework.foundation.domain.properties.utilities.PropertiesPrinter;

public abstract class AbstractPropertiesConfigs {
    public abstract boolean isParentPropertiesNode();

    public void assertProperties(PropertyId propertyId) {
        PropertiesAsserter.assertMandatoryPropertiesConfigs(this, propertyId);
        if (this.isParentPropertiesNode()) {
            printProperties(propertyId);
        }
    }

    public void printProperties(PropertyId propertyId) {
        PropertiesPrinter.printMandatoryPropertiesConfigs(this, propertyId);
    }
}
