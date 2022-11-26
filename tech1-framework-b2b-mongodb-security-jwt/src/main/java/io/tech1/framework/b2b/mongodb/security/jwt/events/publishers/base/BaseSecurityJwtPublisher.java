package io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.base;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.domain.pubsub.AbstractEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtPublisher extends AbstractEventPublisher implements SecurityJwtPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishAuthenticationLogin(EventAuthenticationLogin event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGIN, this.getType(), event.getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAuthenticationLoginFailureUsernamePassword(EventAuthenticationLoginFailureUsernamePassword event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), event.getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAuthenticationLoginFailureUsernameMaskedPassword(EventAuthenticationLoginFailureUsernameMaskedPassword event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), event.getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAuthenticationLogout(EventAuthenticationLogout event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGOUT, this.getType(), event.getSession().getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishRegistrationRegister1(EventRegistrationRegister1 event) {
        LOGGER.debug(SECURITY_JWT_REGISTER1, this.getType(), event.getRequestUserRegistration1().getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionRefreshed(EventSessionRefreshed event) {
        LOGGER.debug(SECURITY_JWT_SESSION_REFRESHED, this.getType(), event.getSession().getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionExpired(EventSessionExpired event) {
        LOGGER.debug(SECURITY_JWT_SESSION_EXPIRED, this.getType(), event.getSession().getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishSessionAddUserRequestMetadata(EventSessionAddUserRequestMetadata event) {
        LOGGER.debug(SECURITY_JWT_SESSION_ADD_USER_REQUEST_METADATA, this.getType(), event.getUsername());
        this.applicationEventPublisher.publishEvent(event);
    }
}
