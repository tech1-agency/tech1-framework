package io.tech1.framework.domain.properties.configs;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertiesConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryTogglePropertiesConfigs;

public abstract class AbstractTogglePropertiesConfigs extends AbstractPropertiesConfigs {
    public abstract boolean isParentPropertiesNode();
    public abstract boolean isEnabled();

    @Override
    public void assertProperties(String propertyName) {
        if (this.isEnabled()) {
            assertMandatoryTogglePropertiesConfigs(this, propertyName);
        } else {
            assertMandatoryPropertiesConfigs(this, propertyName);
        }
        if (this.isParentPropertiesNode()) {
            printProperties(propertyName);
        }
    }
}
