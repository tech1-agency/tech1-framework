package io.tech1.framework.domain.reflections;

import lombok.Data;

import java.util.Arrays;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.uncapitalize;

// Lombok
@Data
public class ReflectionProperty {
    private final String parentPropertyName;
    private final String propertyName;
    private final Object propertyValue;
    private final String readableValue;

    public ReflectionProperty(
            String parentPropertyName,
            String propertyName,
            Object propertyValue
    ) {
        assertNonNullOrThrow(parentPropertyName, invalidAttribute("ReflectionProperty.parentPropertyName"));
        assertNonNullOrThrow(propertyName, invalidAttribute("ReflectionProperty.propertyName"));

        this.parentPropertyName = parentPropertyName;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;

        var isArray = nonNull(this.propertyValue) && this.propertyValue.getClass().isArray();

        // WARNING: draft support only array String[]
        if (isArray) {
            this.readableValue = String.format(
                    "%s.%s: `%s`",
                    uncapitalize(parentPropertyName),
                    uncapitalize(this.propertyName),
                    Arrays.toString((String[]) this.propertyValue)
            );
        } else {
            this.readableValue = String.format(
                    "%s.%s: `%s`",
                    uncapitalize(parentPropertyName),
                    uncapitalize(this.propertyName),
                    this.propertyValue
            );
        }
    }

    public static ReflectionProperty of(
            String parentPropertyName,
            String propertyName,
            Object propertyValue
    ) {
        return new ReflectionProperty(
                parentPropertyName,
                propertyName,
                propertyValue
        );
    }
}
