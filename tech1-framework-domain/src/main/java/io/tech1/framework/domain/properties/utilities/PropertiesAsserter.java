package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.asserts.Asserts;
import io.tech1.framework.domain.constants.LogsConstants;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.MandatoryToggleProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.base.AbstractTogglePropertyConfigs;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import io.tech1.framework.domain.utilities.reflections.ReflectionUtility;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotEmptyOrThrow;
import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.PROPERTIES_ASSERTER_DEBUG;
import static io.tech1.framework.domain.constants.ReflectionsConstants.PROPERTIES_ASSERTION_COMPARATOR;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getPropertyName;
import static java.util.Collections.emptyList;

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
                getMandatoryGetters(propertiesConfigs, propertyName, emptyList())
        );
    }

    public static void assertMandatoryTogglePropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertyName) {
        assertNonNullOrThrow(propertiesConfigs, invalidAttribute(propertyName));
        assertPropertiesConfigs(
                propertiesConfigs,
                propertyName,
                getMandatoryToggleGetters(propertiesConfigs, propertyName, emptyList())
        );
    }

    public static void assertMandatoryPropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName) {
        assertNonNullOrThrow(propertyConfigs, invalidAttribute(propertyName));
        assertPropertyConfigs(
                propertyConfigs,
                propertyName,
                getMandatoryGetters(propertyConfigs, propertyName, emptyList())
        );
    }

    public static void assertMandatoryTogglePropertyConfigs(AbstractTogglePropertyConfigs propertyConfigs, String propertyName) {
        assertNonNullOrThrow(propertyConfigs, invalidAttribute(propertyName));
        assertPropertyConfigs(
                propertyConfigs,
                propertyName,
                getMandatoryToggleGetters(propertyConfigs, propertyName, emptyList())
        );
    }

    // =================================================================================================================
    // GETTERS
    // =================================================================================================================

    public static List<Method> getMandatoryGetters(Object property, String propertyName, List<String> skipProjection) {
        return getGetters(property, propertyName, Set.of(MandatoryProperty.class), skipProjection);
    }

    public static List<Method> getMandatoryToggleGetters(Object property, String propertyName, List<String> skipProjection) {
        return getGetters(property, propertyName, Set.of(MandatoryProperty.class, MandatoryToggleProperty.class), skipProjection);
    }

    public static List<Method> getMandatoryBasedGetters(Object property, String propertyName, List<String> skipProjection) {
        return getGetters(property, propertyName, Set.of(MandatoryProperty.class, NonMandatoryProperty.class, MandatoryToggleProperty.class), skipProjection);
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================

    private static void assertPropertyConfigs(AbstractPropertyConfigs propertyConfigs, String propertyName, List<Method> getters) {
        assertNonNullOrThrow(propertyConfigs, invalidAttribute(propertyName));
        getters.forEach(getter -> {
            var nestedPropertyName = propertyName + "." + getPropertyName(getter);
            try {
                var nestedProperty = getter.invoke(propertyConfigs);
                assertNonNullOrThrow(nestedProperty, invalidAttribute(nestedPropertyName));
                verifyProperty(nestedProperty, nestedPropertyName);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new IllegalArgumentException("Unexpected. Attribute: " + nestedPropertyName);
            }
        });
    }

    private static void assertPropertiesConfigs(AbstractPropertiesConfigs propertiesConfigs, String propertyName, List<Method> getters) {
        assertNonNullOrThrow(propertiesConfigs, invalidAttribute(propertyName));
        getters.forEach(getter -> {
            var nestedPropertyName = propertyName + "." + getPropertyName(getter);
            try {
                var nestedProperty = getter.invoke(propertiesConfigs);
                assertNonNullOrThrow(nestedProperty, invalidAttribute(nestedPropertyName));
                Class<?> propertyClass = nestedProperty.getClass();
                if (AbstractPropertiesConfigs.class.isAssignableFrom(propertyClass)) {
                    ((AbstractPropertiesConfigs) nestedProperty).assertProperties(nestedPropertyName);
                } else if (AbstractPropertyConfigs.class.isAssignableFrom(propertyClass)) {
                    ((AbstractPropertyConfigs) nestedProperty).assertProperties(nestedPropertyName);
                } else {
                    verifyProperty(nestedProperty, nestedPropertyName);
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new IllegalArgumentException("Unexpected. Attribute: " + nestedPropertyName);
            }
        });
    }

    private static void verifyProperty(Object property, String propertyName) {
        if (LogsConstants.DEBUG) {
            LOGGER.info(PROPERTIES_ASSERTER_DEBUG, propertyName, property);
        }
        assertNonNullOrThrow(property, invalidAttribute(propertyName));
        var propertyClass = property.getClass();
        var consumerOpt = ACTIONS.entrySet().stream()
                .filter(entry -> entry.getKey().apply(propertyClass))
                .map(Map.Entry::getValue)
                .findFirst();
        if (consumerOpt.isPresent()) {
            var reflectionProperty = new ReflectionProperty(propertyClass.getSimpleName(), propertyName, property);
            consumerOpt.get().accept(reflectionProperty);
        }
    }

    private static List<Method> getGetters(Object property, String propertyName, Set<Class<? extends Annotation>> presentAnnotations, List<String> skipProjection) {
        assertNonNullOrThrow(property, invalidAttribute(propertyName));
        return ReflectionUtility.getGetters(property).stream()
                .filter(Objects::nonNull)
                .filter(method -> !method.getName().equals("getOrder"))
                .filter(method -> {
                    try {
                        var declaredField = property.getClass().getDeclaredField(getPropertyName(method));
                        var annotationPresent = false;
                        for (Class<? extends Annotation> annotation : presentAnnotations) {
                            if (declaredField.isAnnotationPresent(annotation)) {
                                annotationPresent = true;
                                break;
                            }
                        }
                        return annotationPresent;
                    } catch (NoSuchFieldException ex) {
                        return false;
                    }
                })
                .filter(method -> {
                    var lowerCaseAttribute = method.getName().toLowerCase().replaceAll("^get", "");
                    return !skipProjection.contains(lowerCaseAttribute);
                })
                .sorted(PROPERTIES_ASSERTION_COMPARATOR)
                .toList();
    }
}
