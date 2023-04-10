package io.tech1.framework.incidents.events.publishers.impl;

import io.tech1.framework.domain.pubsub.AbstractEventPublisher;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;
import static io.tech1.framework.domain.enums.Status.COMPLETED;
import static io.tech1.framework.domain.enums.Status.STARTED;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentPublisherImpl extends AbstractEventPublisher implements IncidentPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishResetServerStarted(IncidentSystemResetServerStarted incident) {
        LOGGER.debug(INCIDENT_SYSTEM_RESET_SERVER, this.getType(), incident.getUsername(), STARTED);
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishResetServerCompleted(IncidentSystemResetServerCompleted incident) {
        LOGGER.debug(INCIDENT_SYSTEM_RESET_SERVER, this.getType(), incident.getUsername(), COMPLETED);
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishIncident(Incident incident) {
        LOGGER.debug(INCIDENT, this.getType(), incident.getType());
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishThrowable(IncidentThrowable incident) {
        LOGGER.debug(INCIDENT_THROWABLE, this.getType(), incident.getThrowable().getMessage());
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishThrowable(Throwable throwable) {
        this.publishThrowable(IncidentThrowable.of(throwable));
    }
}
