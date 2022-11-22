package io.tech1.framework.domain.reflections;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class ClassProperty {
    private final String name;
    private final Object value;
    private final String readableValue;

    public ClassProperty(
            String className,
            String name,
            Object value
    ) {
        assertNonNullOrThrow(className, invalidAttribute("ClassProperty.className"));
        assertNonNullOrThrow(name, invalidAttribute("ClassProperty.name"));
        this.name = name;
        this.value = value;
        this.readableValue = String.format("%s.%s: `%s`", className, this.name, this.value);
    }

    public static ClassProperty of(
            String className,
            String propertyName,
            Object propertyValue
    ) {
        return new ClassProperty(
                className,
                propertyName,
                propertyValue
        );
    }

    public static ClassProperty of(
            Class<?> className,
            String propertyName,
            Object propertyValue
    ) {
        return new ClassProperty(
                className.getSimpleName(),
                propertyName,
                propertyValue
        );
    }
}
