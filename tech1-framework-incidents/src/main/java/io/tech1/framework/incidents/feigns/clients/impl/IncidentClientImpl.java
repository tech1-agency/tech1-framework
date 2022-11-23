package io.tech1.framework.incidents.feigns.clients.impl;

import feign.RetryableException;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentClientImpl implements IncidentClient {

    // Definitions
    private final IncidentClientDefinition incidentClientDefinition;

    @Override
    public void registerIncident(Incident incident) {
        try {
            this.incidentClientDefinition.registerIncident(incident);
        } catch (RetryableException ex) {
            this.logIncidentServerOffline(incident.getType(), ex);
        }
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private void logIncidentServerOffline(String incidentType, RetryableException ex) {
        LOGGER.warn("`ops-incident-server` is probably offline. IncidentType: `{}`, Exception: `{}`", incidentType, ex.getMessage());
    }
}
