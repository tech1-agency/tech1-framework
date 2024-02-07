package io.tech1.framework.domain.properties.configs;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertPropertiesConfigs;

public abstract class AbstractPropertiesConfigsV2 {

    public void assertProperties(String parentName) {
        assertPropertiesConfigs(this, parentName);
    }
}
