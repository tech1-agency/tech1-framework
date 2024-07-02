package io.tech1.framework.foundation.incidents.feigns.definitions;

import feign.Headers;
import feign.RequestLine;
import io.tech1.framework.foundation.incidents.domain.Incident;
import org.springframework.http.MediaType;

public interface IncidentClientDefinition {
    @RequestLine("POST /api/incidents/register")
    @Headers("Content-Type: " + MediaType.APPLICATION_JSON_VALUE)
    void registerIncident(Incident incident);
}
