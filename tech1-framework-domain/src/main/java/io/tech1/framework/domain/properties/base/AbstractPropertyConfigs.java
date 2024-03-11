package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.PropertyId;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryBasedConfigs;

@Slf4j
public abstract class AbstractPropertyConfigs {

    public void assertProperties(PropertyId propertyId) {
        assertMandatoryPropertyConfigs(this, propertyId);
    }

    public void printProperties(PropertyId propertyId) {
        printMandatoryBasedConfigs(this, propertyId);
    }
}
