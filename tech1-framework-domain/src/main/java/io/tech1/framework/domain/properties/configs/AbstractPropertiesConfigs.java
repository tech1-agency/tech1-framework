package io.tech1.framework.domain.properties.configs;

import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertNotNullProperties;
import static org.springframework.util.StringUtils.uncapitalize;

@Slf4j
public abstract class AbstractPropertiesConfigs {

    public void assertProperties() {
        var className = this.getClass().getSimpleName();
        assertNotNullProperties(this, uncapitalize(className));
    }
}
