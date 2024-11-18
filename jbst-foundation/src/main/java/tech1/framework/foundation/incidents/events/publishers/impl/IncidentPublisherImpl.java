package tech1.framework.foundation.incidents.events.publishers.impl;

import tech1.framework.foundation.domain.pubsub.AbstractEventPublisher;
import tech1.framework.foundation.incidents.domain.Incident;
import tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import static tech1.framework.foundation.domain.constants.FrameworkLogsConstants.INCIDENT;
import static tech1.framework.foundation.domain.constants.FrameworkLogsConstants.INCIDENT_SYSTEM_RESET_SERVER;
import static tech1.framework.foundation.domain.enums.Status.COMPLETED;
import static tech1.framework.foundation.domain.enums.Status.STARTED;

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
