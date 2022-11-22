package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.utilities.PropertiesAsserter;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.util.StringUtils.uncapitalize;

@Slf4j
public abstract class AbstractPropertiesConfigs {

    public void assertProperties() {
        var className = this.getClass().getSimpleName();
        PropertiesAsserter.assertProperties(this, uncapitalize(className));
    }
}
