package jbst.foundation.incidents.events.subscribers.impl;

import jbst.foundation.domain.pubsub.AbstractEventSubscriber;
import jbst.foundation.incidents.domain.Incident;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import jbst.foundation.incidents.events.subscribers.IncidentSubscriber;
import jbst.foundation.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static jbst.foundation.domain.constants.FrameworkLogsConstants.INCIDENT;

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
