package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ClassProperty;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getGettersClassProperties;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;

@Slf4j
@UtilityClass
public class PropertiesPrinter {
    private static final String INTERPUNCT = "··································································································";

    public static void printProperties(AbstractPropertiesConfigs abstractPropertiesConfigs) {
        printProperties("[Configuration]", abstractPropertiesConfigs);
    }

    public static void printProperties(String prefix, AbstractPropertiesConfigs abstractPropertiesConfigs) {
        LOGGER.info(INTERPUNCT);
        var classProperties = getGettersClassProperties(abstractPropertiesConfigs);
        classProperties.sort(
                comparing(ClassProperty::getPropertyName, comparing((String s) -> !s.equals("enabled")).thenComparing(naturalOrder()))
        );
        classProperties.forEach(property -> LOGGER.info("{} - {}", prefix, property.getReadableValue()));
        LOGGER.info(INTERPUNCT);
    }
}
