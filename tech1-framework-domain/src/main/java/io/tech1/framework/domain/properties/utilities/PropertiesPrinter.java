package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.comparators.ReflectionsComparators.PROPERTIES_PRINTER_COMPARATOR;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.getMandatoryBasedFields;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getProperties;
import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printProperty(ReflectionProperty rf) {
        LOGGER.debug(FRAMEWORK_PROPERTIES_PREFIX + " — {}", rf.getReadableValue());
    }

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertiesConfigsName) {
        var fields = getMandatoryBasedFields(propertiesConfigs, propertiesConfigsName);
        fields.forEach(field -> {
            try {
                var rf = new ReflectionProperty(propertiesConfigsName, field, field.get(propertiesConfigs));
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
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            }
        });
    }

    public static void printMandatoryBasedConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var fields = getMandatoryBasedFields(propertyConfigs, propertyName);
        var rfs = getProperties(propertyConfigs, propertyName, fields);
        rfs.sort(PROPERTIES_PRINTER_COMPARATOR);
        rfs.forEach(PropertiesPrinter::printProperty);
    }
}
