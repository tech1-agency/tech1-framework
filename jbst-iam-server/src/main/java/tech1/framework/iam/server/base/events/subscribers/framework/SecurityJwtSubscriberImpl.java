package tech1.framework.iam.server.base.events.subscribers.framework;

import tech1.framework.foundation.utils.UserMetadataUtils;
import tech1.framework.iam.domain.events.EventAuthenticationLogin;
import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import tech1.framework.iam.events.subscribers.base.BaseSecurityJwtSubscriber;
import tech1.framework.iam.services.BaseUsersSessionsService;
import tech1.framework.iam.services.UsersEmailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityJwtSubscriberImpl extends BaseSecurityJwtSubscriber {

    @Autowired
    public SecurityJwtSubscriberImpl(
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            UsersEmailsService usersEmailsService,
            BaseUsersSessionsService baseUsersSessionsService,
            UserMetadataUtils userMetadataUtils
    ) {
        super(
                securityJwtIncidentPublisher,
                usersEmailsService,
                baseUsersSessionsService,
                userMetadataUtils
        );
    }

    @Override
    public void onAuthenticationLogin(EventAuthenticationLogin event) {
        super.onAuthenticationLogin(event);
        LOGGER.info("[Server] SecurityJwtSubscriber.onAuthenticationLogin(). Username: `{}`", event.username());
    }
}
