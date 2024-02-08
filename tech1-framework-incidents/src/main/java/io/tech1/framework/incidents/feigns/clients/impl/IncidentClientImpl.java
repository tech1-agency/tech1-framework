package io.tech1.framework.incidents.feigns.clients.impl;

import feign.FeignException;
import io.tech1.framework.domain.utilities.printer.PRINTER;
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
        } catch (FeignException ex) {
            PRINTER.error(
                    "[Server]: `ops-incident-server` is probably offline. IncidentType: `{}`. Exception: `{}`",
                    incident.getType(),
                    ex.getMessage()
            );
        }
    }
}
