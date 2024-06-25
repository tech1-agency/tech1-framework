package io.tech1.framework.foundation.domain.properties.base;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.utilities.PropertiesAsserter;

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
