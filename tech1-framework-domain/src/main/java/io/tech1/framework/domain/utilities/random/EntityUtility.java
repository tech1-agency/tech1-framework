package io.tech1.framework.domain.utilities.random;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringThreshold;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringThresholds;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.collections.CollectionUtility.emptyQueue;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.nonNull;

@SuppressWarnings({ "unchecked" })
@UtilityClass
public class EntityUtility {

    private static final Map<Class<?>, Function<Class<?>, Object>> CONSTRUCTORS_RULES = new HashMap<>();

    private static final Deque<Function<Class<?>, Optional<?>>> RANDOM_TYPES_RULES = new LinkedList<>();

    static {
        addConstructorRule(BigDecimal.class, clazz -> randomBigDecimal());
        addConstructorRule(BigInteger.class, clazz -> randomBigInteger());
        addConstructorRule(Long.class, clazz -> randomLong());
        addConstructorRule(Integer.class, clazz -> randomInteger());
        addConstructorRule(Double.class, clazz -> randomDouble());

        addConstructorRule(ZoneId.class, clazz -> randomZoneId());
        addConstructorRule(TimeZone.class, clazz -> randomTimeZone());
        addConstructorRule(Username.class, clazz -> Username.random());
        addConstructorRule(Password.class, clazz -> Password.random());
        addConstructorRule(Email.class, clazz -> Email.random());

        addConstructorRule(IPAddress.class, clazz -> IPAddress.random());
        addConstructorRule(GeoLocation.class, clazz -> randomGeoLocation());
        addConstructorRule(UserAgentDetails.class, clazz -> randomUserAgentDetails());
        addConstructorRule(UserRequestMetadata.class, clazz -> randomUserRequestMetadata());

        addConstructorRule(HardwareMonitoringThreshold.class, clazz -> randomHardwareMonitoringThreshold());
        addConstructorRule(HardwareMonitoringThresholds.class, clazz -> randomHardwareMonitoringThresholds());

        addClassRule(parameterClass -> {
                    var isNotPrimitiveOrWrapper = !parameterClass.isPrimitive() && !containsPrimitiveWrapper(parameterClass);
                    var isNotEnum = !Enum.class.isAssignableFrom(parameterClass);
                    var isNotAnnotation = !Annotation.class.isAssignableFrom(parameterClass);
                    var isNotInterface = !parameterClass.isInterface();
                    return isNotPrimitiveOrWrapper && isNotEnum && isNotAnnotation && isNotInterface;
                },
                EntityUtility::entity
        );

        addClassRule(Class::isEnum, RandomUtility::randomEnumWildcard);

        addClassRule(List.class::equals, parameterClass -> emptyList());
        addClassRule(Set.class::equals, parameterClass -> emptySet());
        addClassRule(Queue.class::equals, parameterClass -> emptyQueue());

        addClassRule(Date.class::equals, parameterClass -> randomDate());
        addClassRule(LocalDate.class::equals, parameterClass -> randomLocalDate());
        addClassRule(LocalDateTime.class::equals, parameterClass -> randomLocalDateTime());

        addClassRule(BigDecimal.class::equals, parameterClass -> randomBigDecimal());

        addClassRule(BigInteger.class::equals, parameterClass -> randomBigInteger());

        addClassRule(Double.class::equals, parameterClass -> randomDouble());
        addClassRule(double.class::equals, parameterClass -> randomDouble());

        addClassRule(Long.class::equals, parameterClass -> randomLong());
        addClassRule(long.class::equals, parameterClass -> randomLong());

        addClassRule(Integer.class::equals, parameterClass -> randomInteger());
        addClassRule(int.class::equals, parameterClass -> randomInteger());

        addClassRule(String.class::equals, parameterClass -> randomString());

        addClassRule(Short.class::equals, parameterClass -> randomShort());
        addClassRule(short.class::equals, parameterClass -> randomShort());

        addClassRule(Boolean.class::equals, parameterClass -> randomBoolean());
        addClassRule(boolean.class::equals, parameterClass -> randomBoolean());

        addClassRule(ZoneId.class::equals, parameterClass -> randomZoneId());
        addClassRule(TimeZone.class::equals, parameterClass -> randomTimeZone());
        addClassRule(Username.class::equals, parameterClass -> Username.random());
        addClassRule(Password.class::equals, parameterClass -> Password.random());
        addClassRule(Email.class::equals, parameterClass -> Email.random());

        addClassRule(IPAddress.class::equals, parameterClass -> IPAddress.random());
        addClassRule(GeoLocation.class::equals, parameterClass -> randomGeoLocation());
        addClassRule(UserAgentDetails.class::equals, parameterClass -> randomUserAgentDetails());
        addClassRule(UserRequestMetadata.class::equals, parameterClass -> randomUserRequestMetadata());

        addClassRule(HardwareMonitoringThreshold.class::equals, parameterClass -> randomHardwareMonitoringThreshold());
        addClassRule(HardwareMonitoringThresholds.class::equals, parameterClass -> randomHardwareMonitoringThresholds());
    }

    public static void addConstructorRule(Class<?> constructorClass, Function<Class<?>, Object> constructionFnc) {
        CONSTRUCTORS_RULES.put(constructorClass, constructionFnc);
    }

    public static void addClassRule(Predicate<Class<?>> newPredicate, Function<Class<?>, ?> newFunction) {
        RANDOM_TYPES_RULES.addFirst(definedRule -> {
            if (newPredicate.test(definedRule)) {
                return Optional.of(newFunction.apply(definedRule));
            } else {
                return Optional.empty();
            }
        });
    }

    public static <T> T entity(Class<? extends T> type) {
        try {
            var clazzFunction = CONSTRUCTORS_RULES.get(type);
            if (nonNull(clazzFunction)) {
                return (T) clazzFunction.apply(type);
            } else {
                return createWithDefaultConstructor(type);
            }
        } catch (ReflectiveOperationException ex1) {
            try {
                return createNoDefaultConstructor(type);
            } catch (ReflectiveOperationException ex2) {
                try {
                    return createPrivateDataLombokBasedConstructor(type);
                } catch (ReflectiveOperationException ex3) {
                    throw new IllegalArgumentException("Please add entity construction rules or extend functionality. Class: `" + type.getCanonicalName() + "`");
                }
            }
        }
    }

    public static <T> List<T> list(Class<? extends T> type, int size) {
        return IntStream.range(0, size).mapToObj(i -> entity(type)).collect(Collectors.toList());
    }

    public static <T> List<T> list345(Class<? extends T> type) {
        return list(type, randomIntegerGreaterThanZeroByBounds(2, 5));
    }

    public static <T> Set<T> set(Class<? extends T> type, int size) {
        return IntStream.range(0, size).mapToObj(i -> entity(type)).collect(Collectors.toSet());
    }

    public static <T> Set<T> set345(Class<? extends T> type) {
        return set(type, randomIntegerGreaterThanZeroByBounds(2, 5));
    }

    // =================================================================================================================
    // PROTECTED METHODS
    // =================================================================================================================
    public static <T> void setObjectFields(T instance) throws ReflectiveOperationException {
        if (instance == null) {
            throw new ReflectiveOperationException("Cannot invoke setter. Instance is null");
        }
        var setters = Stream.of(instance.getClass().getMethods())
                .filter(method -> {
                    Class<?>[] params = method.getParameterTypes();
                    return method.getName().startsWith("set") && params.length > 0;
                }).toList();

        for (Method setter : setters) {
            try {
                var setterClass = setter.getParameterTypes()[0];
                setter.invoke(instance, getRandomValueByClass(setterClass));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                var message = "Cannot invoke setter. Unexpected setter signature";
                throw new ReflectiveOperationException(message);
            }
        }
    }

    public static Object getRandomValueByClass(Class<?> rndClass) {
        Optional<? extends Optional<?>> matches = RANDOM_TYPES_RULES.stream()
                .map(item -> item.apply(rndClass))
                .filter(Optional::isPresent)
                .findFirst();
        if (matches.isPresent()) {
            Optional<?> randomValueOpt = matches.get();
            if (randomValueOpt.isPresent()) {
                return randomValueOpt.get();
            }
        }
        return null;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private static <T> T createWithDefaultConstructor(Class<? extends T> type) throws ReflectiveOperationException {
        T instance = type.getDeclaredConstructor().newInstance();
        setObjectFields(instance);
        return instance;
    }

    private static <T> T createNoDefaultConstructor(Class<? extends T> type) throws ReflectiveOperationException {
        var constructors = type.getConstructors();
        if (constructors.length == 0) {
            throw new ReflectiveOperationException("There is no constructors to initiate instance of type: " + type);
        }
        var constructor = constructors[0];
        var args = Stream.of(constructor.getParameterTypes())
                .map(EntityUtility::getRandomValueByClass)
                .toArray();
        var instance = (T) constructor.newInstance(args);
        setObjectFields(instance);
        return instance;
    }

    private static <T> T createPrivateDataLombokBasedConstructor(Class<? extends T> type) throws ReflectiveOperationException {
        var constructor = type.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        var args = Stream.of(constructor.getParameterTypes())
                .map(EntityUtility::getRandomValueByClass)
                .toArray();
        // ignored setObjectFields() -> @Data constructor construct immutable object
        return (T) constructor.newInstance(args);
    }
}
