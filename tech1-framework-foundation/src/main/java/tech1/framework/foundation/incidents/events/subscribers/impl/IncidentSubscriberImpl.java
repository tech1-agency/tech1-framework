package tech1.framework.foundation.incidents.events.subscribers.impl;

import tech1.framework.foundation.domain.pubsub.AbstractEventSubscriber;
import tech1.framework.foundation.incidents.domain.Incident;
import tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import tech1.framework.foundation.incidents.events.subscribers.IncidentSubscriber;
import tech1.framework.foundation.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static tech1.framework.foundation.domain.constants.FrameworkLogsConstants.INCIDENT;

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
