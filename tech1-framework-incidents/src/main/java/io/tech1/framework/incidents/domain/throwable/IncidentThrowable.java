package io.tech1.framework.incidents.domain.throwable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotBlankOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class IncidentThrowable {
    private Throwable throwable;

    private Method method;
    private List<Object> params;

    private Map<String, Object> attributes;

    public static IncidentThrowable of(
            Throwable throwable
    ) {
        assertNonNullNotBlankOrThrow(throwable, invalidAttribute("ThrowableIncident.throwable"));
        var incident = new IncidentThrowable();
        incident.throwable = throwable;
        return incident;
    }

    public static IncidentThrowable of(
            Throwable throwable,
            Method method,
            List<Object> params
    ) {
        var incident = of(
                throwable
        );
        assertNonNullNotBlankOrThrow(method, invalidAttribute("ThrowableIncident.method"));
        assertNonNullNotBlankOrThrow(params, invalidAttribute("ThrowableIncident.params"));
        incident.method = method;
        incident.params = params;
        return incident;
    }

    public static IncidentThrowable of(
            Throwable throwable,
            Map<String, Object> attributes
    ) {
        assertNonNullNotBlankOrThrow(attributes, invalidAttribute("ThrowableIncident.attributes"));
        var incident = of(
                throwable
        );
        incident.attributes = attributes;
        return incident;
    }
}
