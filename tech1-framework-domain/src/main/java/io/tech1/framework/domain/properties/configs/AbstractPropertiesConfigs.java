package io.tech1.framework.domain.properties.configs;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertNotNullProperties;
import static org.springframework.util.StringUtils.uncapitalize;

public abstract class AbstractPropertiesConfigs {

    public void assertProperties() {
        var className = this.getClass().getSimpleName();
        assertNotNullProperties(this, uncapitalize(className));
    }
}
