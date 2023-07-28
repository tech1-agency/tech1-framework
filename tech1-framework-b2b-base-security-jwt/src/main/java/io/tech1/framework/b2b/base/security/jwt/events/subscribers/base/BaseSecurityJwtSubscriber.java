package io.tech1.framework.b2b.base.security.jwt.events.subscribers.base;

import io.tech1.framework.b2b.base.security.jwt.domain.events.*;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionSessionRefreshedEmail;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.subscribers.SecurityJwtSubscriber;
import io.tech1.framework.b2b.base.security.jwt.services.DeleteService;
import io.tech1.framework.b2b.base.security.jwt.services.UserEmailService;
import io.tech1.framework.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogin;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtSubscriber extends AbstractEventSubscriber implements SecurityJwtSubscriber {

    // Publishers
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UserEmailService userEmailService;
    private final DeleteService deleteService;

    @Override
    public void onAuthenticationLogin(EventAuthenticationLogin event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGIN, this.getType(), event.username());
    }

    @Override
    public void onAuthenticationLoginFailure(EventAuthenticationLoginFailure event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), event.username());
    }

    @Override
    public void onAuthenticationLogout(EventAuthenticationLogout event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGOUT, this.getType(), event.session().username());
    }

    @Override
    public void onRegistration1(EventRegistration1 event) {
        LOGGER.debug(SECURITY_JWT_REGISTER1, this.getType(), event.requestUserRegistration1().username());
    }

    @Override
    public void onRegistration1Failure(EventRegistration1Failure event) {
        LOGGER.debug(SECURITY_JWT_REGISTER1_FAILURE, this.getType(), event.username());
    }

    @Override
    public void onSessionRefreshed(EventSessionRefreshed event) {
        LOGGER.debug(SECURITY_JWT_SESSION_REFRESHED, this.getType(), event.session().username());
    }

    @Override
    public void onSessionExpired(EventSessionExpired event) {
        LOGGER.debug(SECURITY_JWT_SESSION_EXPIRED, this.getType(), event.session().username());
    }

    @Override
    public void onSessionAddUserRequestMetadata(EventSessionAddUserRequestMetadata event) {
        LOGGER.debug(SECURITY_JWT_SESSION_ADD_USER_REQUEST_METADATA, this.getType(), event.username());
        var tuple2 = this.deleteService.saveUserRequestMetadata(event);
        var requestMetadata = tuple2.b();
        if (event.isAuthenticationLoginEndpoint()) {
            this.userEmailService.executeAuthenticationLogin(
                    new FunctionAuthenticationLoginEmail(
                            event.username(),
                            event.email(),
                            requestMetadata
                    )
            );
            this.securityJwtIncidentPublisher.publishAuthenticationLogin(
                    new IncidentAuthenticationLogin(
                            event.username(),
                            requestMetadata
                    )
            );
        }
        if (event.isAuthenticationRefreshTokenEndpoint()) {
            this.userEmailService.executeSessionRefreshed(
                    new FunctionSessionRefreshedEmail(
                            event.username(),
                            event.email(),
                            requestMetadata
                    )
            );
            this.securityJwtIncidentPublisher.publishSessionRefreshed(
                    new IncidentSessionRefreshed(
                            event.username(),
                            requestMetadata
                    )
            );
        }
    }
}
