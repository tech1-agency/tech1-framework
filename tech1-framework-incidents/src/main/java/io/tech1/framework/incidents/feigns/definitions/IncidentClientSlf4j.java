package io.tech1.framework.incidents.feigns.definitions;

import io.tech1.framework.domain.utilities.printer.PRINTER;
import io.tech1.framework.incidents.domain.Incident;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_INCIDENT_PREFIX;

@Slf4j
public class IncidentClientSlf4j implements IncidentClientDefinition {

    @Override
    public void registerIncident(Incident incident) {
        PRINTER.info(FRAMEWORK_INCIDENT_PREFIX + " IncidentType: `{}`", incident.getType());
    }
}
