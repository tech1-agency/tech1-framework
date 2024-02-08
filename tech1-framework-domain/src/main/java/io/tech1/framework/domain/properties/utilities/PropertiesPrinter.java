package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigsV2;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import io.tech1.framework.domain.utilities.reflections.ReflectionUtility;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.constants.ReflectionsConstants.PROPERTIES_PRINTER_COMPARATOR;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.getMandatoryGetters;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.getMandatoryToggleGetters;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getNotNullPropertiesRecursively;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;

// TODO [YYL] add more methods based on new items
@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printMandatoryPropertiesConfigs(AbstractPropertiesConfigsV2 propertiesConfigs, String propertyName) {
        var getters = getMandatoryGetters(propertiesConfigs, propertyName, emptyList());
        // TODO [YYL] complete
    }

    public static void printMandatoryPropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var getters = getMandatoryGetters(propertyConfigs, propertyName, emptyList());
        var properties = ReflectionUtility.getProperties(propertyName, propertyConfigs, getters);
        properties.sort(PROPERTIES_PRINTER_COMPARATOR);
        properties.forEach(property -> LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}",  property.getReadableValue()));
    }

    // TODO [YYL] what about NOT MANDATORY?
    public static void printMandatoryTogglePropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var getters = getMandatoryToggleGetters(propertyConfigs, propertyName, emptyList());
        var properties = ReflectionUtility.getProperties(propertyName, propertyConfigs, getters);
        properties.sort(PROPERTIES_PRINTER_COMPARATOR);
        properties.forEach(property -> LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}",  property.getReadableValue()));
    }

//    @Deprecated
//    private static void printProperties(AbstractPropertiesConfigs abstractPropertiesConfigs) {
//        printProperties(abstractPropertiesConfigs, "[Configuration]");
//    }
//
//    @Deprecated
//    private static void printProperties(AbstractPropertiesConfigs abstractPropertiesConfigs, String prefix) {
//        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
//        var parentKey = abstractPropertiesConfigs.getClass().getSimpleName();
//        var properties = getNotNullPropertiesRecursively(abstractPropertiesConfigs, parentKey);
//        properties.sort(comparing(ReflectionProperty::getReadableValue));
//        properties.forEach(property -> LOGGER.info("{} - {}", prefix, property.getReadableValue()));
//        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
//    }
}
