package io.tech1.framework.foundation.incidents.feigns.definitions;

import io.tech1.framework.foundation.incidents.domain.Incident;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class IncidentClientDefinitionSlf4j implements IncidentClientDefinition {

    @Override
    public void registerIncident(@NotNull Incident incident) {
        incident.print();
    }
}
