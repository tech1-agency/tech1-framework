package jbst.foundation.domain.properties.configs;

import jbst.foundation.domain.base.PropertyId;
import jbst.foundation.domain.properties.utilities.PropertiesAsserter;

public abstract class AbstractTogglePropertiesConfigs extends AbstractPropertiesConfigs {
    public abstract boolean isParentPropertiesNode();
    public abstract boolean isEnabled();

    @Override
    public void assertProperties(PropertyId propertyId) {
        if (this.isEnabled()) {
            PropertiesAsserter.assertMandatoryTogglePropertiesConfigs(this, propertyId);
        } else {
            PropertiesAsserter.assertMandatoryPropertiesConfigs(this, propertyId);
        }
        if (this.isParentPropertiesNode()) {
            printProperties(propertyId);
        }
    }
}
