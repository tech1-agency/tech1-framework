package io.tech1.framework.domain.properties.configs;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertiesConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryTogglePropertiesConfigs;

public abstract class AbstractTogglePropertiesConfigs extends AbstractPropertiesConfigsV2 {
    abstract boolean isEnabled();

    @Override
    public void assertProperties(String propertyName) {
        if (this.isEnabled()) {
            assertMandatoryTogglePropertiesConfigs(this, propertyName);
        } else {
            assertMandatoryPropertiesConfigs(this, propertyName);
        }
    }
}
