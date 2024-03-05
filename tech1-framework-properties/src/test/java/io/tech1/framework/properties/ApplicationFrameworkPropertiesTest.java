package io.tech1.framework.properties;

import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getGetters;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationFrameworkPropertiesTest {

    @Test
    void applicationFrameworkPropertiesTest() {
        // Arrange
        var context = new ApplicationFrameworkPropertiesContext();
        var applicationFrameworkProperties = context.applicationFrameworkProperties();

        // Act
        var getters = getGetters(applicationFrameworkProperties);

        // Assert
        assertThat(getters).hasSize(14);
        getters.forEach(getter -> {
            try {
                var propertiesConfigs = getter.invoke(applicationFrameworkProperties);
                assertThat(propertiesConfigs).isNotNull();
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
