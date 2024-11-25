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

@SuppressWarnings("LoggingSimilarMessage")
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtPublisher extends AbstractEventPublisher implements SecurityJwtPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishAuthenticationLogin(EventAuthenticationLogin event) {
        LOGGER.debug(USER_ACTION, event.username(), "[pub] login");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAuthenticationLoginFailure(EventAuthenticationLoginFailure event) {
        LOGGER.debug(USER_ACTION, event.username(), "[pub] login failure");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAuthenticationLogout(EventAuthenticationLogout event) {
        LOGGER.debug(USER_ACTION, event.username(), "[pub] logout");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishRegistration1(EventRegistration1 event) {
        LOGGER.debug(USER_ACTION, event.requestUserRegistration1().username(), "[pub] register1");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishRegistration1Failure(EventRegistration1Failure event) {
        LOGGER.debug(USER_ACTION, event.username(), "[pub] register1 failure");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionRefreshed(EventSessionRefreshed event) {
        LOGGER.debug(USER_ACTION, event.session().username(), "[pub] session refreshed");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionExpired(EventSessionExpired event) {
        LOGGER.debug(USER_ACTION, event.session().username(), "[pub] session expired");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionUserRequestMetadataAdd(EventSessionUserRequestMetadataAdd event) {
        LOGGER.debug(USER_ACTION, event.username(), "[pub] session user request metadata add");
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionUserRequestMetadataRenew(EventSessionUserRequestMetadataRenew event) {
        LOGGER.debug(USER_ACTION, event.username(), "[pub] session user request metadata renew, sessionId: " + event.session().id());
        this.applicationEventPublisher.publishEvent(event);
    }
}
