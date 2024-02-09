package io.tech1.framework.domain.reflections;

import lombok.Data;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.Arrays;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.uncapitalize;

// Lombok
@Data
public class ReflectionProperty {
    private static final String READABLE_PROPERTY = "%s: `%s`";

    private final Field field;
    private final String propertyName;
    private final String treePropertyName;
    private final Object propertyValue;
    private final String readableValue;

    public ReflectionProperty(String parentPropertyName, Field field, Object propertyValue) {
        assertNonNullOrThrow(parentPropertyName, invalidAttribute("ReflectionProperty.parentPropertyName"));

        this.field = field;
        this.propertyName = field.getName();
        this.treePropertyName = uncapitalize(parentPropertyName) + "." + uncapitalize(propertyName);
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
            this.readableValue = READABLE_PROPERTY.formatted(this.treePropertyName,Arrays.toString(castedPropertyValue));
        } else if (isZoneId) {
            var castedPropertyValue = (ZoneId) this.propertyValue;
            this.readableValue = READABLE_PROPERTY.formatted(this.treePropertyName, castedPropertyValue.getId());
        } else {
            this.readableValue = READABLE_PROPERTY.formatted(this.treePropertyName, this.propertyValue);
        }
    }
}
