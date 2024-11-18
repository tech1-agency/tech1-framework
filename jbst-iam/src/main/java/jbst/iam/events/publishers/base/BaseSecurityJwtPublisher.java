package jbst.iam.events.publishers.base;

import jbst.foundation.domain.pubsub.AbstractEventPublisher;
import jbst.iam.domain.events.*;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static jbst.foundation.domain.constants.JbstConstants.Logs.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtPublisher extends AbstractEventPublisher implements SecurityJwtPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishAuthenticationLogin(EventAuthenticationLogin event) {
        LOGGER.debug(EVENTS_AUTHENTICATION_LOGIN, this.getType(), event.username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAuthenticationLoginFailure(EventAuthenticationLoginFailure event) {
        LOGGER.debug(EVENTS_AUTHENTICATION_LOGIN_FAILURE, this.getType(), event.username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAuthenticationLogout(EventAuthenticationLogout event) {
        LOGGER.debug(EVENTS_AUTHENTICATION_LOGOUT, this.getType(), event.username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishRegistration1(EventRegistration1 event) {
        LOGGER.debug(EVENTS_REGISTER1, this.getType(), event.requestUserRegistration1().username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishRegistration1Failure(EventRegistration1Failure event) {
        LOGGER.debug(EVENTS_REGISTER1_FAILURE, this.getType(), event.username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionRefreshed(EventSessionRefreshed event) {
        LOGGER.debug(EVENTS_SESSION_REFRESHED, this.getType(), event.session().username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionExpired(EventSessionExpired event) {
        LOGGER.debug(EVENTS_SESSION_EXPIRED, this.getType(), event.session().username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionUserRequestMetadataAdd(EventSessionUserRequestMetadataAdd event) {
        LOGGER.debug(EVENTS_SESSION_ADD_USER_REQUEST_METADATA, this.getType(), event.username());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionUserRequestMetadataRenew(EventSessionUserRequestMetadataRenew event) {
        LOGGER.debug(EVENTS_SESSION_RENEW_USER_REQUEST_METADATA, this.getType(), event.username(), event.session().id());
        this.applicationEventPublisher.publishEvent(event);
    }
}
