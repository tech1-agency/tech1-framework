package io.tech1.framework.incidents.events.subscribers.impl;

import io.tech1.framework.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.Registration1FailureIncident;
import io.tech1.framework.incidents.domain.registration.Registration1Incident;
import io.tech1.framework.incidents.domain.session.SessionExpiredIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
import io.tech1.framework.incidents.events.subscribers.IncidentSubscriber;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;

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
    public void onEvent(ThrowableIncident throwableIncident) {
        LOGGER.debug(INCIDENT_THROWABLE, this.getType(), throwableIncident.getThrowable().getMessage());
        var incident = this.incidentConverter.convert(throwableIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(AuthenticationLoginIncident authenticationLoginIncident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN, this.getType(), authenticationLoginIncident.getUsername());
        var incident = this.incidentConverter.convert(authenticationLoginIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(AuthenticationLoginFailureUsernamePasswordIncident authenticationLoginFailureUsernamePasswordIncident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), authenticationLoginFailureUsernamePasswordIncident.getUsername());
        var incident = this.incidentConverter.convert(authenticationLoginFailureUsernamePasswordIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(AuthenticationLoginFailureUsernameMaskedPasswordIncident authenticationLoginFailureUsernameMaskedPasswordIncident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), authenticationLoginFailureUsernameMaskedPasswordIncident.getUsername());
        var incident = this.incidentConverter.convert(authenticationLoginFailureUsernameMaskedPasswordIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(AuthenticationLogoutMinIncident authenticationLogoutMinIncident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), authenticationLogoutMinIncident.getUsername());
        var incident = this.incidentConverter.convert(authenticationLogoutMinIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(AuthenticationLogoutFullIncident authenticationLogoutFullIncident) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), authenticationLogoutFullIncident.getUsername());
        var incident = this.incidentConverter.convert(authenticationLogoutFullIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(SessionRefreshedIncident sessionRefreshedIncident) {
        LOGGER.debug(INCIDENT_SESSION_REFRESHED, this.getType(), sessionRefreshedIncident.getUsername());
        var incident = this.incidentConverter.convert(sessionRefreshedIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(SessionExpiredIncident sessionExpiredIncident) {
        LOGGER.debug(INCIDENT_SESSION_EXPIRED, this.getType(), sessionExpiredIncident.getUsername());
        var incident = this.incidentConverter.convert(sessionExpiredIncident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(Registration1Incident registration1Incident) {
        LOGGER.debug(INCIDENT_REGISTER1, this.getType(), registration1Incident.getUsername());
        var incident = this.incidentConverter.convert(registration1Incident);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(Registration1FailureIncident registration1FailureIncident) {
        LOGGER.debug(INCIDENT_REGISTER1_FAILURE, this.getType(), registration1FailureIncident.getUsername());
        var incident = this.incidentConverter.convert(registration1FailureIncident);
        this.incidentClient.registerIncident(incident);
    }
}
