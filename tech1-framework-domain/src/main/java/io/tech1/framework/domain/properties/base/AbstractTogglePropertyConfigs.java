package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.PropertyId;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryTogglePropertyConfigs;

public abstract class AbstractTogglePropertyConfigs extends AbstractPropertyConfigs {
    protected abstract boolean isEnabled();

    @Override
    public void assertProperties(PropertyId propertyId) {
        if (this.isEnabled()) {
            assertMandatoryTogglePropertyConfigs(this, propertyId);
        } else {
            assertMandatoryPropertyConfigs(this, propertyId);
        }
    }
}
