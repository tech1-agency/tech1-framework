package io.tech1.framework.foundation.domain.properties;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static io.tech1.framework.foundation.utilities.reflections.ReflectionUtility.getGetters;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationFrameworkPropertiesTest {

    @Test
    void applicationFrameworkPropertiesTest() {
        // Arrange
        var context = new ApplicationFrameworkPropertiesTestsHardcodedContext();
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
