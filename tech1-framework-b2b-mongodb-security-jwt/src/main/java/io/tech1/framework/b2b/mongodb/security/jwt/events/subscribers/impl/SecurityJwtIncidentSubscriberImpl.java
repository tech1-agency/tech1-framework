package io.tech1.framework.b2b.mongodb.security.jwt.events.subscribers.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.events.subscribers.SecurityJwtIncidentSubscriber;
import io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters.SecurityJwtIncidentConverter;
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
    // Converters
    private final SecurityJwtIncidentConverter securityJwtIncidentConverter;

    @Override
    public void onEvent(IncidentAuthenticationLogin incidentAuthenticationLogin) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN, this.getType(), incidentAuthenticationLogin.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentAuthenticationLogin);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incidentAuthenticationLoginFailureUsernamePassword.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentAuthenticationLoginFailureUsernamePassword);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incidentAuthenticationLoginFailureUsernameMaskedPassword.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentAuthenticationLoginFailureUsernameMaskedPassword);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incidentAuthenticationLogoutMin.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentAuthenticationLogoutMin);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull) {
        LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incidentAuthenticationLogoutFull.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentAuthenticationLogoutFull);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentRegistration1 incidentRegistration1) {
        LOGGER.debug(INCIDENT_REGISTER1, this.getType(), incidentRegistration1.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentRegistration1);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentRegistration1Failure incidentRegistration1Failure) {
        LOGGER.debug(INCIDENT_REGISTER1_FAILURE, this.getType(), incidentRegistration1Failure.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentRegistration1Failure);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentSessionRefreshed incidentSessionRefreshed) {
        LOGGER.debug(INCIDENT_SESSION_REFRESHED, this.getType(), incidentSessionRefreshed.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentSessionRefreshed);
        this.incidentClient.registerIncident(incident);
    }

    @Override
    public void onEvent(IncidentSessionExpired incidentSessionExpired) {
        LOGGER.debug(INCIDENT_SESSION_EXPIRED, this.getType(), incidentSessionExpired.getUsername());
        var incident = this.securityJwtIncidentConverter.convert(incidentSessionExpired);
        this.incidentClient.registerIncident(incident);
    }
}
