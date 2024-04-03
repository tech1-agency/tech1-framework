package io.tech1.framework.incidents.events.subscribers.impl;

import io.tech1.framework.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.subscribers.IncidentSubscriber;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.INCIDENT;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.INCIDENT_THROWABLE;
import static io.tech1.framework.incidents.domain.IncidentAttributes.Keys.TYPE;
import static io.tech1.framework.incidents.domain.IncidentAttributes.Keys.USERNAME;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentSubscriberImpl extends AbstractEventSubscriber implements IncidentSubscriber {

    // Clients
    private final IncidentClient incidentClient;

    @Override
    public void onEvent(IncidentSystemResetServerStarted incident) {
        this.onEvent(
                new Incident(
                        Map.of(
                                TYPE, "Reset Server Started",
                                USERNAME, incident.username()
                        )
        ));
    }

    @Override
    public void onEvent(IncidentSystemResetServerCompleted incident) {
        this.onEvent(
                new Incident(
                        Map.of(
                                TYPE, "Reset Server Completed",
                                USERNAME, incident.username()
                        )
                ));
    }

    @Override
    public void onEvent(Incident incident) {
        LOGGER.debug(INCIDENT, this.getType(), incident.getType());
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentThrowable incident) {
        LOGGER.debug(INCIDENT_THROWABLE, this.getType(), incident.getThrowable().getMessage());
        this.incidentClient.registerIncident(new Incident(incident));
    }
}
