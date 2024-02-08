package io.tech1.framework.domain.reflections;

import lombok.Data;

import java.time.ZoneId;
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

        // supports only String[] and ZoneId (on 5+ cases refactoring or extraction required)
        var isArray = nonNull(this.propertyValue) && this.propertyValue.getClass().isArray();
        boolean isArrayOfStrings;
        if (isArray) {
            var array = (Object[]) this.propertyValue;
            isArrayOfStrings = array[0] instanceof String;
        } else {
            isArrayOfStrings = false;
        }
        var isZoneId = nonNull(this.propertyValue) && this.propertyValue instanceof ZoneId;

        if (isArrayOfStrings) {
            var castedPropertyValue = (String[]) this.propertyValue;
            this.readableValue = String.format(
                    "%s.%s: `%s`",
                    uncapitalize(parentPropertyName),
                    uncapitalize(this.propertyName),
                    Arrays.toString(castedPropertyValue)
            );
        } else if (isZoneId) {
            var castedPropertyValue = (ZoneId) this.propertyValue;
            this.readableValue = String.format(
                    "%s.%s: `%s`",
                    uncapitalize(parentPropertyName),
                    uncapitalize(this.propertyName),
                    castedPropertyValue.getId()
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
}
