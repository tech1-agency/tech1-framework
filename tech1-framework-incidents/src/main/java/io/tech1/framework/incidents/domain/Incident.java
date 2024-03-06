package io.tech1.framework.incidents.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_INCIDENT_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static java.util.Objects.nonNull;

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
