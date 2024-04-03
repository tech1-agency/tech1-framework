package io.tech1.framework.b2b.base.security.jwt.events.subscribers.impl;

import io.tech1.framework.b2b.base.security.jwt.events.subscribers.SecurityJwtIncidentSubscriber;
import io.tech1.framework.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtIncidentSubscriberImpl extends AbstractEventSubscriber implements SecurityJwtIncidentSubscriber {

    // Clients
    private final IncidentClient incidentClient;

    @Override
    public void onEvent(IncidentAuthenticationLogin incident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN, this.getType(), incident.username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentAuthenticationLoginFailureUsernamePassword incident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.credentials().username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.credentials().username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentAuthenticationLogoutMin incident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentAuthenticationLogoutFull incident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentRegistration1 incident) {
        LOGGER.debug(INCIDENT_REGISTER1, this.getType(), incident.username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentRegistration1Failure incident) {
        LOGGER.debug(INCIDENT_REGISTER1_FAILURE, this.getType(), incident.username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentSessionRefreshed incident) {
        LOGGER.debug(INCIDENT_SESSION_REFRESHED, this.getType(), incident.username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }

    @Override
    public void onEvent(IncidentSessionExpired incident) {
        LOGGER.debug(INCIDENT_SESSION_EXPIRED, this.getType(), incident.username());
        this.incidentClient.registerIncident(incident.getPlainIncident());
    }
}
