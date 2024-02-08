package io.tech1.framework.domain.properties.base;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryTogglePropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesPrinter.printMandatoryTogglePropertyConfigs;

public abstract class AbstractTogglePropertyConfigs extends AbstractPropertyConfigs {
    protected abstract boolean isEnabled();

    @Override
    public void assertProperties(String propertyName) {
        if (this.isEnabled()) {
            assertMandatoryTogglePropertyConfigs(this, propertyName);
        } else {
            assertMandatoryPropertyConfigs(this, propertyName);
        }
    }

    @Override
    public void printProperties(String propertyName) {
        if (this.isEnabled()) {
            printMandatoryTogglePropertyConfigs(this, propertyName);
        } else {
            printMandatoryPropertyConfigs(this, propertyName);
        }
    }
}
