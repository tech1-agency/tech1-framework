package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.base.PropertyId;
import jbst.foundation.domain.properties.utilities.PropertiesAsserter;

public abstract class AbstractTogglePropertyConfigs extends AbstractPropertyConfigs {
    protected abstract boolean isEnabled();

    @Override
    public void assertProperties(PropertyId propertyId) {
        if (this.isEnabled()) {
            PropertiesAsserter.assertMandatoryTogglePropertyConfigs(this, propertyId);
        } else {
            PropertiesAsserter.assertMandatoryPropertyConfigs(this, propertyId);
        }
    }
}
