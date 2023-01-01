package io.tech1.framework.incidents.converters;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;

public interface IncidentConverter {
    Incident convert(IncidentThrowable incidentThrowable);
}
