package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.base.PropertyId;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertiesConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryTogglePropertiesConfigs;

public abstract class AbstractTogglePropertiesConfigs extends AbstractPropertiesConfigs {
    public abstract boolean isParentPropertiesNode();
    public abstract boolean isEnabled();

    @Override
    public void assertProperties(PropertyId propertyId) {
        if (this.isEnabled()) {
            assertMandatoryTogglePropertiesConfigs(this, propertyId);
        } else {
            assertMandatoryPropertiesConfigs(this, propertyId);
        }
        if (this.isParentPropertiesNode()) {
            printProperties(propertyId);
        }
    }
}
