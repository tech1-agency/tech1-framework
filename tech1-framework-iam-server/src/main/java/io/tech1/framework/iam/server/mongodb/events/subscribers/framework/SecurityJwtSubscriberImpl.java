package io.tech1.framework.iam.server.mongodb.events.subscribers.framework;

import io.tech1.framework.foundation.utils.UserMetadataUtils;
import io.tech1.framework.iam.domain.events.EventAuthenticationLogin;
import io.tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.iam.events.subscribers.base.BaseSecurityJwtSubscriber;
import io.tech1.framework.iam.services.UsersEmailsService;
import io.tech1.framework.iam.services.mongodb.MongoBaseUsersSessionsService;
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
            MongoBaseUsersSessionsService baseUsersSessionsService,
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
