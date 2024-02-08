package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.utilities.reflections.ReflectionUtility;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.constants.ReflectionsConstants.PROPERTIES_PRINTER_COMPARATOR;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.getMandatoryBasedGetters;
import static java.util.Collections.emptyList;

@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertyName) {
        var getters = getMandatoryBasedGetters(propertiesConfigs, propertyName, emptyList());

    }

    public static void printMandatoryPropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var getters = getMandatoryBasedGetters(propertyConfigs, propertyName, emptyList());
        var properties = ReflectionUtility.getProperties(propertyName, propertyConfigs, getters);
        properties.sort(PROPERTIES_PRINTER_COMPARATOR);
        properties.forEach(property -> LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}",  property.getReadableValue()));
    }

    public static void printMandatoryTogglePropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var getters = getMandatoryBasedGetters(propertyConfigs, propertyName, emptyList());
        var properties = ReflectionUtility.getProperties(propertyName, propertyConfigs, getters);
        properties.sort(PROPERTIES_PRINTER_COMPARATOR);
        properties.forEach(property -> LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}",  property.getReadableValue()));
    }
}
