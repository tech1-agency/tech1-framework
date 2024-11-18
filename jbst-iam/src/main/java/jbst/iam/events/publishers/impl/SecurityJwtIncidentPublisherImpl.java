package jbst.iam.events.publishers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.domain.pubsub.AbstractEventPublisher;
import tech1.framework.foundation.incidents.domain.authetication.*;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionExpired;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionRefreshed;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;

import static tech1.framework.foundation.domain.constants.FrameworkLogsConstants.*;
import static tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtIncidentPublisherImpl extends AbstractEventPublisher implements SecurityJwtIncidentPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void publishAuthenticationLogin(IncidentAuthenticationLogin incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGIN)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGIN);
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernamePassword(IncidentAuthenticationLoginFailureUsernamePassword incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.credentials().username());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernameMaskedPassword(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.credentials().username());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLogoutMin(IncidentAuthenticationLogoutMin incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGOUT_MIN)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGOUT_MIN);
        }
    }

    @Override
    public void publishAuthenticationLogoutFull(IncidentAuthenticationLogoutFull incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(AUTHENTICATION_LOGOUT)) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, AUTHENTICATION_LOGOUT);
        }
    }

    @Override
    public void publishRegistration1(IncidentRegistration1 incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(REGISTER1)) {
            LOGGER.debug(INCIDENT_REGISTER1, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, REGISTER1);
        }
    }

    @Override
    public void publishRegistration1Failure(IncidentRegistration1Failure incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(REGISTER1_FAILURE)) {
            LOGGER.debug(INCIDENT_REGISTER1_FAILURE, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, REGISTER1_FAILURE);
        }
    }

    @Override
    public void publishSessionRefreshed(IncidentSessionRefreshed incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(SESSION_REFRESHED)) {
            LOGGER.debug(INCIDENT_SESSION_REFRESHED, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, SESSION_REFRESHED);
        }
    }

    @Override
    public void publishSessionExpired(IncidentSessionExpired incident) {
        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getIncidentsConfigs().isEnabled(SESSION_EXPIRED)) {
            LOGGER.debug(INCIDENT_SESSION_EXPIRED, this.getType(), incident.username());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, SESSION_EXPIRED);
        }
    }
}
