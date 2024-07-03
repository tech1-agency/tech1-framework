package io.tech1.framework.foundation.incidents.feigns.clients;

import io.tech1.framework.foundation.incidents.domain.Incident;

public interface IncidentClient {
    void registerIncident(Incident incident);
}
