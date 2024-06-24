package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.comparators.ReflectionsComparators.PROPERTIES_PRINTER_COMPARATOR;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.getMandatoryBasedFields;
import static io.tech1.framework.foundation.utilities.reflections.ReflectionUtility.getProperties;
import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printProperty(ReflectionProperty rf) {
        LOGGER.debug(FRAMEWORK_PROPERTIES_PREFIX + " — {}", rf.getReadableValue());
    }

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, PropertyId propertyId) {
        var fields = getMandatoryBasedFields(propertiesConfigs, propertyId);
        fields.forEach(field -> {
            try {
                var rf = new ReflectionProperty(propertyId, field, field.get(propertiesConfigs));
                if (isNull(rf.getPropertyValue())) {
                    printProperty(rf);
                } else {
                    var nestedPropertyClass = rf.getPropertyValue().getClass();
                    if (AbstractPropertiesConfigs.class.isAssignableFrom(nestedPropertyClass)) {
                        ((AbstractPropertiesConfigs) rf.getPropertyValue()).printProperties(rf.getTreePropertyId());
                    } else if (AbstractPropertyConfigs.class.isAssignableFrom(nestedPropertyClass)) {
                        ((AbstractPropertyConfigs) rf.getPropertyValue()).printProperties(rf.getTreePropertyId());
                    } else {
                        printProperty(rf);
                    }
                }
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            }
        });
    }

    public static void printMandatoryBasedConfigs(AbstractPropertyConfigs propertyConfigs, PropertyId propertyId) {
        var fields = getMandatoryBasedFields(propertyConfigs, propertyId);
        var rfs = getProperties(propertyConfigs, propertyId, fields);
        rfs.sort(PROPERTIES_PRINTER_COMPARATOR);
        rfs.forEach(PropertiesPrinter::printProperty);
    }
}
