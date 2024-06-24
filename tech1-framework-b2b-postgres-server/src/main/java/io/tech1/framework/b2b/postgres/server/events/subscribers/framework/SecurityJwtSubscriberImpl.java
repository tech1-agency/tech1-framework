package io.tech1.framework.b2b.postgres.server.events.subscribers.framework;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLogin;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.subscribers.base.BaseSecurityJwtSubscriber;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.UsersEmailsService;
import io.tech1.framework.foundation.utilities.utils.UserMetadataUtils;
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
