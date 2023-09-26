package io.tech1.framework.b2b.base.security.jwt.events.subscribers.base;

import io.tech1.framework.b2b.base.security.jwt.domain.events.*;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionSessionRefreshedEmail;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.subscribers.SecurityJwtSubscriber;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.UsersEmailsService;
import io.tech1.framework.domain.pubsub.AbstractEventSubscriber;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogin;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtSubscriber extends AbstractEventSubscriber implements SecurityJwtSubscriber {

    // Publishers
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UsersEmailsService usersEmailsService;
    private final BaseUsersSessionsService baseUsersSessionsService;

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
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGOUT, this.getType(), event.username());
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
    public void onSessionUserRequestMetadataAdd(EventSessionUserRequestMetadataAdd event) {
        LOGGER.debug(SECURITY_JWT_SESSION_ADD_USER_REQUEST_METADATA, this.getType(), event.username());
        var session = this.baseUsersSessionsService.saveUserRequestMetadata(event);
        var metadata = session.metadata();
        if (event.isAuthenticationLoginEndpoint()) {
            this.usersEmailsService.executeAuthenticationLogin(
                    new FunctionAuthenticationLoginEmail(
                            event.username(),
                            event.email(),
                            metadata
                    )
            );
            this.securityJwtIncidentPublisher.publishAuthenticationLogin(
                    new IncidentAuthenticationLogin(
                            event.username(),
                            metadata
                    )
            );
        }
        if (event.isAuthenticationRefreshTokenEndpoint()) {
            this.usersEmailsService.executeSessionRefreshed(
                    new FunctionSessionRefreshedEmail(
                            event.username(),
                            event.email(),
                            metadata
                    )
            );
            this.securityJwtIncidentPublisher.publishSessionRefreshed(
                    new IncidentSessionRefreshed(
                            event.username(),
                            metadata
                    )
            );
        }
    }

    @Override
    public void onSessionUserRequestMetadataRenewCron(EventSessionUserRequestMetadataRenewCron event) {
        LOGGER.debug(SECURITY_JWT_SESSION_RENEW_CRON_USER_REQUEST_METADATA, this.getType(), event.username(), event.session().id());
        this.baseUsersSessionsService.saveUserRequestMetadata(event);
    }

    @Override
    public void onSessionUserRequestMetadataRenewManually(EventSessionUserRequestMetadataRenewManually event) {
        LOGGER.debug(SECURITY_JWT_SESSION_RENEW_MANUALLY_USER_REQUEST_METADATA, this.getType(), event.username(), event.session().id());
        this.baseUsersSessionsService.saveUserRequestMetadata(event);
    }
}
