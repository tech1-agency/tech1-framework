package io.tech1.framework.domain.properties.configs;

public abstract class AbstractPropertiesToggleConfigs extends AbstractPropertiesConfigs {
    public abstract boolean isEnabled();

    @Override
    public void assertProperties() {
        if (this.isEnabled()) {
            super.assertProperties();
        }
    }
}
