package io.tech1.framework.incidents.events.subscribers.impl;

import io.tech1.framework.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.subscribers.IncidentSubscriber;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.INCIDENT;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.INCIDENT_THROWABLE;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentSubscriberImpl extends AbstractEventSubscriber implements IncidentSubscriber {

    // Clients
    private final IncidentClient incidentClient;
    // Converters
    private final IncidentConverter incidentConverter;

    @Override
    public void onEvent(Incident incident) {
        LOGGER.debug(INCIDENT, this.getType(), incident.getType());
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentThrowable incidentThrowable) {
        LOGGER.debug(INCIDENT_THROWABLE, this.getType(), incidentThrowable.getThrowable().getMessage());
        var incident = this.incidentConverter.convert(incidentThrowable);
        this.incidentClient.registerIncident(incident);
    }
}
