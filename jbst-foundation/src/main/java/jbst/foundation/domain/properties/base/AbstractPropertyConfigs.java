package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.base.PropertyId;
import jbst.foundation.domain.properties.utilities.PropertiesAsserter;
import jbst.foundation.domain.properties.utilities.PropertiesPrinter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPropertyConfigs {

    public void assertProperties(PropertyId propertyId) {
        PropertiesAsserter.assertMandatoryPropertyConfigs(this, propertyId);
    }

    public void printProperties(PropertyId propertyId) {
        PropertiesPrinter.printMandatoryBasedConfigs(this, propertyId);
    }
}
