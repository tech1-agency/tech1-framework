package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import io.tech1.framework.domain.utilities.reflections.ReflectionUtility;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.constants.ReflectionsConstants.PROPERTIES_PRINTER_COMPARATOR;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.getMandatoryBasedGetters;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getPropertyName;
import static java.util.Collections.emptyList;

@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertyName) {
        var getters = getMandatoryBasedGetters(propertiesConfigs, propertyName, emptyList());
        getters.forEach(getter -> {
            var nextedPropertyName = propertyName + "." + getPropertyName(getter);
            try {
                var nestedProperty = getter.invoke(propertiesConfigs);
                Class<?> propertyClass = nestedProperty.getClass();
                if (AbstractPropertiesConfigs.class.isAssignableFrom(propertyClass)) {
                    ((AbstractPropertiesConfigs) nestedProperty).printProperties(nextedPropertyName);
                } else if (AbstractPropertyConfigs.class.isAssignableFrom(propertyClass)) {
                    ((AbstractPropertyConfigs) nestedProperty).printProperties(nextedPropertyName);
                } else {
                    var rf = new ReflectionProperty(propertyName, getPropertyName(getter), nestedProperty);
                    LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}", rf.getReadableValue());
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new IllegalArgumentException("Unexpected. Attribute: " + nextedPropertyName);
            }
        });
    }

    public static void printMandatoryBasedConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var getters = getMandatoryBasedGetters(propertyConfigs, propertyName, emptyList());
        var rfs = ReflectionUtility.getProperties(propertyConfigs, propertyName, getters);
        rfs.sort(PROPERTIES_PRINTER_COMPARATOR);
        rfs.forEach(property -> LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}",  property.getReadableValue()));
    }
}
