package jbst.iam.events.subscribers.base;

import jbst.foundation.domain.base.UsernamePasswordCredentials;
import jbst.foundation.domain.pubsub.AbstractEventSubscriber;
import jbst.foundation.incidents.domain.authetication.IncidentAuthenticationLogin;
import jbst.foundation.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernameMaskedPassword;
import jbst.foundation.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernamePassword;
import jbst.foundation.incidents.domain.session.IncidentSessionRefreshed;
import jbst.foundation.utils.UserMetadataUtils;
import jbst.iam.domain.events.*;
import jbst.iam.domain.functions.FunctionAuthenticationLoginEmail;
import jbst.iam.domain.functions.FunctionSessionRefreshedEmail;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.subscribers.SecurityJwtSubscriber;
import jbst.iam.services.BaseUsersSessionsService;
import jbst.iam.services.UsersEmailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static jbst.foundation.domain.constants.FrameworkLogsConstants.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityJwtSubscriber extends AbstractEventSubscriber implements SecurityJwtSubscriber {

    // Publishers
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Services
    private final UsersEmailsService usersEmailsService;
    private final BaseUsersSessionsService baseUsersSessionsService;
    // Utils
    private final UserMetadataUtils userMetadataUtils;

    @Override
    public void onAuthenticationLogin(EventAuthenticationLogin event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGIN, this.getType(), event.username());
    }

    @Override
    public void onAuthenticationLoginFailure(EventAuthenticationLoginFailure event) {
        LOGGER.debug(SECURITY_JWT_AUTHENTICATION_LOGIN_FAILURE, this.getType(), event.username());
        var userRequestMetadata = this.userMetadataUtils.getUserRequestMetadataProcessed(
                event.ipAddress(),
                event.userAgentHeader()
        );
        this.securityJwtIncidentPublisher.publishAuthenticationLoginFailureUsernamePassword(
                new IncidentAuthenticationLoginFailureUsernamePassword(
                        new UsernamePasswordCredentials(
                                event.username(),
                                event.password()
                        ),
                        userRequestMetadata
                )
        );
        this.securityJwtIncidentPublisher.publishAuthenticationLoginFailureUsernameMaskedPassword(
                new IncidentAuthenticationLoginFailureUsernameMaskedPassword(
                        UsernamePasswordCredentials.mask5(
                                event.username(),
                                event.password()
                        ),
                        userRequestMetadata
                )
        );
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
    public void onSessionUserRequestMetadataRenew(EventSessionUserRequestMetadataRenew event) {
        LOGGER.debug(SECURITY_JWT_SESSION_RENEW_USER_REQUEST_METADATA, this.getType(), event.username(), event.session().id());
        this.baseUsersSessionsService.saveUserRequestMetadata(event);
    }
}
