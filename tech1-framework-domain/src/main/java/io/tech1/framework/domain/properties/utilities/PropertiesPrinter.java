package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import io.tech1.framework.domain.utilities.printer.PRINTER;
import io.tech1.framework.domain.utilities.reflections.ReflectionUtility;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.constants.ReflectionsConstants.PROPERTIES_PRINTER_COMPARATOR;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.getMandatoryBasedGetters;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getPropertyName;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printProperty(ReflectionProperty rf) {
        PRINTER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}", rf.getReadableValue());
    }

    public static void printProperty(Object property, String propertyName) {
        PRINTER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}: `{}`", propertyName, property);
    }

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertiesConfigsName) {
        var getters = getMandatoryBasedGetters(propertiesConfigs, propertiesConfigsName, emptyList());
        getters.forEach(getter -> {
            try {
                var rf = new ReflectionProperty(propertiesConfigsName, getPropertyName(getter), getter.invoke(propertiesConfigs));
                if (isNull(rf.getPropertyValue())) {
                    printProperty(rf);
                } else {
                    var nestedPropertyClass = rf.getPropertyValue().getClass();
                    if (AbstractPropertiesConfigs.class.isAssignableFrom(nestedPropertyClass)) {
                        ((AbstractPropertiesConfigs) rf.getPropertyValue()).printProperties(rf.getTreePropertyName());
                    } else if (AbstractPropertyConfigs.class.isAssignableFrom(nestedPropertyClass)) {
                        ((AbstractPropertyConfigs) rf.getPropertyValue()).printProperties(rf.getTreePropertyName());
                    } else {
                        printProperty(rf);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new IllegalArgumentException(ex);
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
