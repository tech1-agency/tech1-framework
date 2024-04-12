package io.tech1.framework.incidents.domain.throwable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

// TODO [YYL-incidents] check obsolete-ness
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

    public static IncidentThrowable of(@NotNull Throwable throwable) {
        var incident = new IncidentThrowable();
        incident.throwable = throwable;
        return incident;
    }

    public static IncidentThrowable of(@NotNull Throwable throwable, @NotNull Method method, @NotNull List<Object> params) {
        var incident = of(throwable);
        incident.method = method;
        incident.params = params;
        return incident;
    }

    public static IncidentThrowable of(@NotNull Throwable throwable, @NotNull Map<String, Object> attributes) {
        var incident = of(throwable);
        incident.attributes = attributes;
        return incident;
    }
}
