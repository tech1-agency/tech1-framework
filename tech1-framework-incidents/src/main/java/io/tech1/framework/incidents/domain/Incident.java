package io.tech1.framework.incidents.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.TreeMap;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.nonNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class Incident {
    private final Map<String, Object> attributes;

    public Incident() {
        this.attributes = new TreeMap<>();
    }

    public static Incident copyOf(Incident incident) {
        assertNonNullOrThrow(incident, invalidAttribute("Incident.incident"));
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
