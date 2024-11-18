package jbst.foundation.incidents.events.publishers.impl;

import jbst.foundation.domain.pubsub.AbstractEventPublisher;
import jbst.foundation.incidents.domain.Incident;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import static jbst.foundation.domain.constants.JbstConstants.Logs.INCIDENT;
import static jbst.foundation.domain.constants.JbstConstants.Logs.INCIDENT_SYSTEM_RESET_SERVER;
import static jbst.foundation.domain.enums.Status.COMPLETED;
import static jbst.foundation.domain.enums.Status.STARTED;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentPublisherImpl extends AbstractEventPublisher implements IncidentPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishResetServerStarted(IncidentSystemResetServerStarted incident) {
        LOGGER.debug(INCIDENT_SYSTEM_RESET_SERVER, this.getType(), incident.username(), STARTED);
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishResetServerCompleted(IncidentSystemResetServerCompleted incident) {
        LOGGER.debug(INCIDENT_SYSTEM_RESET_SERVER, this.getType(), incident.username(), COMPLETED);
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishIncident(Incident incident) {
        LOGGER.debug(INCIDENT, this.getType(), incident.getType());
        this.applicationEventPublisher.publishEvent(incident);
    }
}
