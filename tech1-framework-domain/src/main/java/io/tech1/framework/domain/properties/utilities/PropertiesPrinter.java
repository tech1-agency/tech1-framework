package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import io.tech1.framework.domain.utilities.reflections.ReflectionUtility;
import io.tech1.framework.domain.utilities.system.SystemProperties;
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

    public static void printProperty(ReflectionProperty rf) {
        if (SystemProperties.isPropertiesDebugEnabled()) {
            LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}", rf.getReadableValue());
        }
    }

    public static void printProperty(Object property, String propertyName) {
        if (SystemProperties.isPropertiesDebugEnabled()) {
            LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}: `{}`", propertyName, property);
        }
    }

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertiesConfigsName) {
        var getters = getMandatoryBasedGetters(propertiesConfigs, propertiesConfigsName, emptyList());
        getters.forEach(getter -> {
            var nestedPropertyName = getPropertyName(getter);
            var treePropertyName = propertiesConfigsName + "." + nestedPropertyName;
            try {
                var nestedProperty = getter.invoke(propertiesConfigs);
                Class<?> propertyClass = nestedProperty.getClass();
                if (AbstractPropertiesConfigs.class.isAssignableFrom(propertyClass)) {
                    ((AbstractPropertiesConfigs) nestedProperty).printProperties(treePropertyName);
                } else if (AbstractPropertyConfigs.class.isAssignableFrom(propertyClass)) {
                    ((AbstractPropertyConfigs) nestedProperty).printProperties(treePropertyName);
                } else {
                    var rf = new ReflectionProperty(propertiesConfigsName, nestedPropertyName, nestedProperty);
                    printProperty(rf);
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new IllegalArgumentException("Unexpected. Attribute: " + treePropertyName);
            }
        });
    }

    public static void printMandatoryBasedConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var getters = getMandatoryBasedGetters(propertyConfigs, propertyName, emptyList());
        var rfs = ReflectionUtility.getProperties(propertyConfigs, propertyName, getters);
        rfs.sort(PROPERTIES_PRINTER_COMPARATOR);
        rfs.forEach(PropertiesPrinter::printProperty);
    }
}
