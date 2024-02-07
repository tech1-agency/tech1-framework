package io.tech1.framework.domain.properties.base;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryPropertyConfigs;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertMandatoryTogglePropertyConfigs;

public abstract class AbstractTogglePropertyConfigs extends AbstractPropertyConfigs {
    abstract boolean isEnabled();

    @Override
    public void assertProperties(String parentName) {
        if (this.isEnabled()) {
            assertMandatoryTogglePropertyConfigs(this, parentName);
        } else {
            assertMandatoryPropertyConfigs(this, parentName);
        }
    }

}
