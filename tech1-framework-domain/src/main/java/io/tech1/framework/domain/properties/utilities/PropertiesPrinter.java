package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigsV2;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getNotNullProperties;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getNotNullPropertiesRecursively;
import static java.util.Comparator.comparing;

// TODO [YYL] add more methods based on new items
@Slf4j
@UtilityClass
public class PropertiesPrinter {

    public static void printPropertiesConfigs(AbstractPropertiesConfigsV2 propertiesConfigs, String propertyName) {
        System.out.println("TODO --> printPropertiesConfigs: " + propertyName);
    }

    public static void printPropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        var properties = getNotNullProperties(propertyConfigs, propertyName);
        properties.sort(comparing(ReflectionProperty::getReadableValue));
        properties.forEach(property -> LOGGER.info(FRAMEWORK_PROPERTIES_PREFIX + " — {}",  property.getReadableValue()));
    }

    @Deprecated
    public static void printProperties(AbstractPropertiesConfigs abstractPropertiesConfigs) {
        printProperties(abstractPropertiesConfigs, "[Configuration]");
    }

    public static void printProperties(AbstractPropertiesConfigs abstractPropertiesConfigs, String prefix) {
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        var parentKey = abstractPropertiesConfigs.getClass().getSimpleName();
        var properties = getNotNullPropertiesRecursively(abstractPropertiesConfigs, parentKey);
        properties.sort(comparing(ReflectionProperty::getReadableValue));
        properties.forEach(property -> LOGGER.info("{} - {}", prefix, property.getReadableValue()));
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
    }
}
