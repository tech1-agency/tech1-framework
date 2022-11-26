package io.tech1.framework.incidents.events.publishers.impl;

import io.tech1.framework.domain.pubsub.AbstractEventPublisher;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.Register1FailureIncident;
import io.tech1.framework.incidents.domain.registration.Register1Incident;
import io.tech1.framework.incidents.domain.session.SessionExpiredIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentPublisherImpl extends AbstractEventPublisher implements IncidentPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void publishIncident(Incident incident) {
        LOGGER.debug(INCIDENT, this.getType(), incident.getType());
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishThrowable(ThrowableIncident incident) {
        LOGGER.debug(INCIDENT_THROWABLE, this.getType(), incident.getThrowable().getMessage());
        this.applicationEventPublisher.publishEvent(incident);
    }

    @Override
    public void publishThrowable(Throwable throwable) {
        this.publishThrowable(ThrowableIncident.of(throwable));
    }

    @Override
    public void publishAuthenticationLogin(AuthenticationLoginIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLogin().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, "Authentication Login");
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernamePassword(AuthenticationLoginFailureUsernamePasswordIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLoginFailureUsernamePassword().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLoginFailureUsernameMaskedPassword(AuthenticationLoginFailureUsernameMaskedPasswordIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLoginFailureUsernameMaskedPassword().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        }
    }

    @Override
    public void publishAuthenticationLogoutMin(AuthenticationLogoutMinIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLogout().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, "Authentication Logout");
        }
    }

    @Override
    public void publishAuthenticationLogoutFull(AuthenticationLogoutFullIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getLogout().isEnabled()) {
            LOGGER.debug(INCIDENT_AUTHENTICATION_LOGOUT, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, "Authentication Logout");
        }
    }

    @Override
    public void publishSessionRefreshed(SessionRefreshedIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getSessionRefreshed().isEnabled()) {
            LOGGER.debug(INCIDENT_SESSION_REFRESHED, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, "Session Refreshed");
        }
    }

    @Override
    public void publishSessionExpired(SessionExpiredIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getSessionExpired().isEnabled()) {
            LOGGER.debug(INCIDENT_SESSION_EXPIRED, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, "Session Expired");
        }
    }

    @Override
    public void publishRegistration1(Register1Incident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getRegister1().isEnabled()) {
            LOGGER.debug(INCIDENT_REGISTER1, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, "Register1");
        }
    }

    @Override
    public void publishRegistration1Failure(Register1FailureIncident incident) {
        if (this.applicationFrameworkProperties.getIncidentConfigs().getFeatures().getRegister1Failure().isEnabled()) {
            LOGGER.debug(INCIDENT_REGISTER1_FAILURE, this.getType(), incident.getUsername());
            this.applicationEventPublisher.publishEvent(incident);
        } else {
            LOGGER.warn(INCIDENT_FEATURE_DISABLED, "Register1 Failure");
        }
    }
}
