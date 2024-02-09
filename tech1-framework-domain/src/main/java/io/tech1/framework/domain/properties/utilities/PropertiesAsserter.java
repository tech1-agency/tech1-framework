package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.asserts.Asserts;
import io.tech1.framework.domain.properties.annotations.MandatoryMapProperty;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.MandatoryToggleProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.base.AbstractTogglePropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import io.tech1.framework.domain.utilities.enums.EnumUtility;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.constants.ReflectionsConstants.PROPERTIES_ASSERTION_COMPARATOR;
import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoiningWildcard;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttributeRequiredMissingValues;
import static org.apache.commons.collections4.SetUtils.disjunction;

@Slf4j
@UtilityClass
public class PropertiesAsserter {
    private static final Map<Function<Class<?>, Boolean>, Consumer<ReflectionProperty>> ACTIONS = new HashMap<>();

    static {
        ACTIONS.put(Date.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(LocalDate.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(LocalDateTime.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(ChronoUnit.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(TimeUnit.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(Boolean.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(Short.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(Integer.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(Long.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(BigInteger.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(BigDecimal.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(String.class::equals, Asserts::assertNonNullPropertyOrThrow);
        ACTIONS.put(Collection.class::isAssignableFrom, cp -> assertNonNullNotEmptyOrThrow((Collection<?>) cp.getPropertyValue(), invalidAttribute(cp.getPropertyName())));
    }

    // =================================================================================================================
    // Assertions
    // =================================================================================================================

    public static void assertMandatoryPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertyName) {
        assertNonNullOrThrow(propertiesConfigs, invalidAttribute(propertyName));
        assertPropertiesConfigs(
                propertiesConfigs,
                propertyName,
                getMandatoryFields(propertiesConfigs, propertyName)
        );
    }

    public static void assertMandatoryTogglePropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertyName) {
        assertNonNullOrThrow(propertiesConfigs, invalidAttribute(propertyName));
        assertPropertiesConfigs(
                propertiesConfigs,
                propertyName,
                getMandatoryToggleFields(propertiesConfigs, propertyName)
        );
    }

    public static void assertMandatoryPropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        assertNonNullOrThrow(propertyConfigs, invalidAttribute(propertyName));
        assertPropertyConfigs(
                propertyConfigs,
                propertyName,
                getMandatoryFields(propertyConfigs, propertyName)
        );
    }

    public static void assertMandatoryTogglePropertyConfigs(AbstractTogglePropertyConfigs propertyConfigs, String propertyName) {
        assertNonNullOrThrow(propertyConfigs, invalidAttribute(propertyName));
        assertPropertyConfigs(
                propertyConfigs,
                propertyName,
                getMandatoryToggleFields(propertyConfigs, propertyName)
        );
    }

    // =================================================================================================================
    // GETTERS
    // =================================================================================================================

    public static List<Field> getMandatoryFields(Object property, String propertyName) {
        return getFields(property, propertyName, Set.of(MandatoryProperty.class));
    }

    public static List<Field> getMandatoryToggleFields(Object property, String propertyName) {
        return getFields(property, propertyName, Set.of(MandatoryProperty.class, MandatoryToggleProperty.class));
    }

    public static List<Field> getMandatoryBasedFields(Object property, String propertyName) {
        return getFields(property, propertyName, Set.of(MandatoryProperty.class, NonMandatoryProperty.class, MandatoryToggleProperty.class));
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================

    private static void assertPropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyConfigsName, List<Field> fields) {
        assertNonNullOrThrow(propertyConfigs, invalidAttribute(propertyConfigsName));
        fields.forEach(field -> {
            try {
                var rf = new ReflectionProperty(propertyConfigsName, field, field.get(propertyConfigs));
                assertNonNullPropertyOrThrow(rf);
                verifyProperty(rf);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            }
        });
    }

    private static void assertPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertiesConfigsName, List<Field> fields) {
        assertNonNullOrThrow(propertiesConfigs, invalidAttribute(propertiesConfigsName));
        fields.forEach(field -> {
            try {
                var rf = new ReflectionProperty(propertiesConfigsName, field, field.get(propertiesConfigs));
                assertNonNullPropertyOrThrow(rf);
                var nestedPropertyClass = rf.getPropertyValue().getClass();
                if (AbstractPropertiesConfigs.class.isAssignableFrom(nestedPropertyClass)) {
                    ((AbstractPropertiesConfigs) rf.getPropertyValue()).assertProperties(rf.getTreePropertyName());
                } else if (AbstractPropertyConfigs.class.isAssignableFrom(nestedPropertyClass)) {
                    ((AbstractPropertyConfigs) rf.getPropertyValue()).assertProperties(rf.getTreePropertyName());
                } else {
                    verifyProperty(rf);
                }
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    private static void verifyProperty(ReflectionProperty rf) {
        var property = rf.getPropertyValue();
        if (rf.getField().isAnnotationPresent(MandatoryMapProperty.class)) {
            var annotation = rf.getField().getAnnotation(MandatoryMapProperty.class);
            Class<? extends Enum<?>> keySetClass = annotation.keySetClass();
            var castedProperty = (Map) property;
            var size = (annotation.size() == -1) ? keySetClass.getEnumConstants().length : annotation.size();
            //noinspection unchecked
            assertTrueOrThrow(
                    castedProperty.size() == size,
                    invalidAttributeRequiredMissingValues(
                            rf.getTreePropertyName(),
                            baseJoiningWildcard(keySetClass),
                            baseJoining(disjunction(castedProperty.keySet(), EnumUtility.setWildcard(keySetClass)))
                    )
            );
        }
        ACTIONS.entrySet().stream()
                .filter(entry -> entry.getKey().apply(property.getClass()))
                .map(Map.Entry::getValue)
                .findFirst()
                .ifPresent(consumer -> consumer.accept(rf));
    }

    private static List<Field> getFields(Object property, String propertyName, Set<Class<? extends Annotation>> presentAnnotations) {
        assertNonNullOrThrow(property, invalidAttribute(propertyName));
        return Stream.of(property.getClass().getDeclaredFields())
                .filter(Objects::nonNull)
                .map(field -> {
                    for (Class<? extends Annotation> annotation : presentAnnotations) {
                        if (field.isAnnotationPresent(annotation)) {
                            field.setAccessible(true);
                            return field;
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(PROPERTIES_ASSERTION_COMPARATOR)
                .toList();
    }
}
