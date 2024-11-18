package jbst.foundation.incidents.feigns.clients;

import jbst.foundation.incidents.domain.Incident;

public interface IncidentClient {
    void registerIncident(Incident incident);
}
