package io.tech1.framework.incidents.feigns.clients;

import io.tech1.framework.incidents.domain.Incident;

public interface IncidentClient {
    void registerIncident(Incident incident);
}
