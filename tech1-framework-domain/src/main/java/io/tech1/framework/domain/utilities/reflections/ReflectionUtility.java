package io.tech1.framework.domain.utilities.reflections;

import io.tech1.framework.domain.reflections.ClassProperty;
import io.tech1.framework.domain.tuples.Tuple2;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.uncapitalize;

@SuppressWarnings("unchecked")
@UtilityClass
public class ReflectionUtility {

    public static <T, S> void setPrivateField(T object, String nameField, S valueField) throws NoSuchFieldException, IllegalAccessException {
        var field = object.getClass().getDeclaredField(nameField);
        field.setAccessible(true);
        field.set(object, valueField);
    }

    public static <T, S> void setPrivateFieldOfSuperClass(T object, String nameField, S valueField, int numberOfSuperClass) throws NoSuchFieldException, IllegalAccessException {
        Class<?> expectedClass = object.getClass();
        for (var i = 0 ; i < numberOfSuperClass; i++) {
            expectedClass = expectedClass.getSuperclass();
        }
        var field = expectedClass.getDeclaredField(nameField);
        field.setAccessible(true);
        field.set(object, valueField);
    }

    public static <T, S> S getPrivateField(T object, String nameField) throws NoSuchFieldException, IllegalAccessException {
        var field = object.getClass().getDeclaredField(nameField);
        field.setAccessible(true);
        return (S) field.get(object);
    }

    public static <T, S> S getPrivateFieldOfSuperClass(T object, String nameField, int numberOfSuperClass) throws NoSuchFieldException, IllegalAccessException {
        Class<?> expectedClass = object.getClass();
        for (var i = 0 ; i < numberOfSuperClass; i++) {
            expectedClass = expectedClass.getSuperclass();
        }
        var field = expectedClass.getDeclaredField(nameField);
        field.setAccessible(true);
        return (S) field.get(object);
    }

    public static void objectFieldHook(Class<?> type, Object hook, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        var field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        var modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, hook);
    }

    public static void assertFieldsNotNull(Object object) {
        var declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
            } catch (IllegalAccessException ex) {
                // ignored
            }
            assert nonNull(value) : "Field " + field.getName() + " is null";
        }
    }

    public static <T> List<String> getAllNullFields(T object) {
        var declaredFields = object.getClass().getDeclaredFields();
        final List<String> nullFields = new ArrayList<>();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                final Object o = field.get(object);
                if (isNull(o)) {
                    nullFields.add(field.getName());
                }
            } catch (IllegalAccessException ex) {
                // ignored
            }
        }
        return nullFields;
    }

    public static List<Method> getGetters(Object object) {
        return Stream.of(object.getClass().getMethods())
                .filter(method -> !method.getName().equals("getClass"))
                .filter(method -> method.getParameterTypes().length == 0)
                .filter(method -> {
                    var methodName = method.getName();
                    return methodName.startsWith("get") || methodName.startsWith("is");
                }).collect(Collectors.toList());
    }

    public static List<ClassProperty> getGettersClassProperties(Object object, boolean skipNull) {
        var className = object.getClass().getSimpleName();
        var getters = getGetters(object);
        return getters.stream()
                .map(getter -> {
                    try {
                        var getterName = getter.getName();
                        var propertyName = uncapitalize(getterName
                                .replace("get", "")
                                .replace("is", "")
                        );
                        var getterValueObj = getter.invoke(object);
                        if (isNull(getterValueObj) && skipNull) {
                            return null;
                        } else {
                            var getterValue = nonNull(getterValueObj) ? getterValueObj : "[null]";
                            return ClassProperty.of(
                                    className,
                                    propertyName,
                                    getterValue
                            );
                        }
                    } catch (IllegalAccessException | InvocationTargetException | RuntimeException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ClassProperty::getName))
                .collect(Collectors.toList());
    }

    public static Object getFieldValueOrNull(Object object, Field field, List<Method> getters) {
        var getterOpt = getters.stream()
                .filter(method -> method.getName().equalsIgnoreCase("get" + field.getName()))
                .reduce((a, b) -> {
                    throw new IllegalArgumentException("Reflection utility. Multiple getters: " + a + ", " + b);
                });
        if (getterOpt.isPresent()) {
            try {
                Object value = getterOpt.get().invoke(object);
                return isNull(value) ? null : value;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Reflection utility. Missing getter, field: " + field.getName());
        }
    }

    public static Tuple2<Field, Object> getFieldTuple2(Object object, Field field, List<Method> getters) {
        var fieldValueOrNull = getFieldValueOrNull(object, field, getters);
        return Tuple2.of(field, fieldValueOrNull);
    }
}
