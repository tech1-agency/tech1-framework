package jbst.iam.events.publishers.impl;

import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.domain.pubsub.AbstractEventPublisher;
import jbst.foundation.incidents.domain.authetication.*;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import jbst.foundation.incidents.domain.session.IncidentSessionExpired;
import jbst.foundation.incidents.domain.session.IncidentSessionRefreshed;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static jbst.foundation.domain.constants.JbstConstants.Logs.*;
import static jbst.foundation.domain.properties.base.SecurityJwtIncidentType.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtIncidentPublisherImpl extends AbstractEventPublisher implements SecurityJwtIncidentPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final JbstProperties jbstProperties;

    @Override
    public void publishAuthenticationLogin(IncidentAuthenticationLogin incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGIN)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGIN);
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernamePassword(IncidentAuthenticationLoginFailureUsernamePassword incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.credentials().username());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernameMaskedPassword(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.credentials().username());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLogoutMin(IncidentAuthenticationLogoutMin incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGOUT_MIN)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGOUT_MIN);
        }
    }

    @Override
    public void publishAuthenticationLogoutFull(IncidentAuthenticationLogoutFull incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGOUT)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGOUT);
        }
    }

    @Override
    public void publishRegistration1(IncidentRegistration1 incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(REGISTER1)) {
            LOGGER.debug(INCIDENT_REGISTER1, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, REGISTER1);
        }
    }

    @Override
    public void publishRegistration1Failure(IncidentRegistration1Failure incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(REGISTER1_FAILURE)) {
            LOGGER.debug(INCIDENT_REGISTER1_FAILURE, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, REGISTER1_FAILURE);
        }
    }

    @Override
    public void publishSessionRefreshed(IncidentSessionRefreshed incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(SESSION_REFRESHED)) {
            LOGGER.debug(INCIDENT_SESSION_REFRESHED, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, SESSION_REFRESHED);
        }
    }

    @Override
    public void publishSessionExpired(IncidentSessionExpired incident) {
        if (this.jbstProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(SESSION_EXPIRED)) {
            LOGGER.debug(INCIDENT_SESSION_EXPIRED, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, SESSION_EXPIRED);
        }
    }
}
