package jbst.foundation.domain.properties;

import jbst.foundation.configurations.ConfigurationPropertiesJbstHardcoded;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static jbst.foundation.utilities.reflections.ReflectionUtility.getGetters;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationFrameworkPropertiesTest {

    @Test
    void applicationFrameworkPropertiesTest() {
        // Arrange
        var context = new ConfigurationPropertiesJbstHardcoded();
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
