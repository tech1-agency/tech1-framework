package io.tech1.framework.incidents.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_INCIDENT_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.exceptions.TraceUtility.getTrace;
import static io.tech1.framework.incidents.domain.IncidentAttributes.Keys.*;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class Incident {
    private final Map<String, Object> attributes;

    public Incident() {
        this.attributes = new TreeMap<>();
    }

    public Incident(Map<String, Object> attributes) {
        this.attributes = new TreeMap<>();
        this.addAll(attributes);
    }

    public Incident(SecurityJwtIncidentType type, Username username) {
        this();
        this.addType(type.getValue());
        this.addUsername(username);
    }

    public Incident(SecurityJwtIncidentType type, Username username, Password password) {
        this();
        this.addType(type.getValue());
        this.addUsername(username);
        this.addPassword(password);
    }

    public Incident(SecurityJwtIncidentType type, Username username, UserRequestMetadata userRequestMetadata) {
        this();
        this.addType(type.getValue());
        this.addUsername(username);

        var tupleResponseException = userRequestMetadata.getException();
        if (!tupleResponseException.isOk()) {
            this.add(EXCEPTION, tupleResponseException.getMessage());
        }

        var whereTuple3 = userRequestMetadata.getWhereTuple3();
        this.add(IP_ADDRESS, whereTuple3.a());
        this.add(COUNTRY_FLAG, whereTuple3.b());
        this.add(WHERE, whereTuple3.c());

        var whatTuple2 = userRequestMetadata.getWhatTuple2();
        this.add(BROWSER, whatTuple2.a());
        this.add(WHAT, whatTuple2.b());
    }

    public Incident(IncidentThrowable incidentThrowable) {
        this();
        this.add(IncidentAttributes.Keys.TYPE, IncidentAttributes.IncidentsTypes.THROWABLE);

        var throwable = incidentThrowable.getThrowable();
        this.add(IncidentAttributes.Keys.EXCEPTION, throwable.getClass());
        this.add(IncidentAttributes.Keys.TRACE, getTrace(throwable));
        this.add(IncidentAttributes.Keys.MESSAGE, throwable.getMessage());

        var method = incidentThrowable.getMethod();
        if (nonNull(method)) {
            this.add(IncidentAttributes.Keys.METHOD, method.toString());
        }

        var params = incidentThrowable.getParams();
        if (!isEmpty(params)) {
            this.add(IncidentAttributes.Keys.PARAMS, params.stream().map(Object::toString).collect(Collectors.joining(", ")));
        }

        if (!isEmpty(incidentThrowable.getAttributes())) {
            incidentThrowable.getAttributes().forEach(this::add);
        }
    }

    public static Incident copyOf(@NotNull Incident incident) {
        var instance = new Incident();
        instance.addAll(incident.getAttributes());
        return instance;
    }

    public void add(String key, Object value) {
        this.attributes.put(key, value);
    }

    public void addAll(Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
    }

    public void print() {
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        LOGGER.info(FRAMEWORK_INCIDENT_PREFIX + " IncidentType: `{}`", this.getType());
        this.attributes.entrySet().stream()
                .filter(entry -> !IncidentAttributes.Keys.TYPE.equals(entry.getKey()))
                .forEach(entry -> LOGGER.info(entry.getKey() + " — " + entry.getValue()));
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
    }

    public void addType(String type) {
        this.add(IncidentAttributes.Keys.TYPE, type);
    }

    public void addUsername(Username username) {
        this.add(IncidentAttributes.Keys.USERNAME, username);
    }

    public void addPassword(Password password) {
        this.add(IncidentAttributes.Keys.PASSWORD, password);
    }

    @JsonIgnore
    public String getType() {
        var attribute = this.attributes.get(IncidentAttributes.Keys.TYPE);
        if (nonNull(attribute)) {
            return attribute.toString();
        } else {
            return UNKNOWN;
        }
    }

    @JsonIgnore
    public Username getUsername() {
        var attribute = this.attributes.get(IncidentAttributes.Keys.USERNAME);
        if (nonNull(attribute)) {
            return Username.of(attribute.toString());
        } else {
            return Username.of(UNKNOWN);
        }
    }
}
