package io.tech1.framework.properties;

import io.tech1.framework.properties.tests.contexts.ApplicationPlatformPropertiesContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getGetters;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationPlatformPropertiesTest {

    @Test
    public void assertApplicationPlatformProperties() {
        // Arrange
        var context = new ApplicationPlatformPropertiesContext();
        var applicationPlatformProperties = context.applicationPlatformProperties();

        // Act
        var getters = getGetters(applicationPlatformProperties);

        // Assert
        assertThat(getters).isNotNull();
        assertThat(getters).hasSize(10);
        getters.forEach(getter -> {
            try {
                var propertiesConfigs = getter.invoke(applicationPlatformProperties);
                assertThat(propertiesConfigs).isNotNull();
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
