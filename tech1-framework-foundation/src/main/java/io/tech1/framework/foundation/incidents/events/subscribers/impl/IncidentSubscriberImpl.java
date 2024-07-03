package io.tech1.framework.foundation.incidents.events.subscribers.impl;

import io.tech1.framework.foundation.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.foundation.incidents.domain.Incident;
import io.tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.foundation.incidents.events.subscribers.IncidentSubscriber;
import io.tech1.framework.foundation.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static io.tech1.framework.foundation.domain.constants.FrameworkLogsConstants.INCIDENT;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentSubscriberImpl extends AbstractEventSubscriber implements IncidentSubscriber {

    // Clients
    private final IncidentClient incidentClient;

    @Override
    public void onEvent(IncidentSystemResetServerStarted incident) {
        this.onEvent(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentSystemResetServerCompleted incident) {
        this.onEvent(incident.getPlainIncident());
    }

    @Override
    public void onEvent(Incident incident) {
        LOGGER.debug(INCIDENT, this.getType(), incident.getType());
        this.incidentClient.registerIncident(incident);
    }
}
