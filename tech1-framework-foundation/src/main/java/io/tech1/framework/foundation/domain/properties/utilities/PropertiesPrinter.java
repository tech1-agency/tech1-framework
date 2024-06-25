package io.tech1.framework.foundation.domain.properties.utilities;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.foundation.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.foundation.domain.reflections.ReflectionProperty;
import io.tech1.framework.foundation.domain.comparators.ReflectionsComparators;
import io.tech1.framework.foundation.domain.constants.FrameworkLogsConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.foundation.utilities.reflections.ReflectionUtility.getProperties;
import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printProperty(ReflectionProperty rf) {
        LOGGER.debug(FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX + " — {}", rf.getReadableValue());
    }

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, PropertyId propertyId) {
        var fields = PropertiesAsserter.getMandatoryBasedFields(propertiesConfigs, propertyId);
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
        var fields = PropertiesAsserter.getMandatoryBasedFields(propertyConfigs, propertyId);
        var rfs = getProperties(propertyConfigs, propertyId, fields);
        rfs.sort(ReflectionsComparators.PROPERTIES_PRINTER_COMPARATOR);
        rfs.forEach(PropertiesPrinter::printProperty);
    }
}
