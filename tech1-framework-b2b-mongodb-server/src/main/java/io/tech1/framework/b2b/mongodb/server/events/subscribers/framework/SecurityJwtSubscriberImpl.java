package io.tech1.framework.b2b.mongodb.server.events.subscribers.framework;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLogin;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.subscribers.base.BaseSecurityJwtSubscriber;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.UsersEmailsService;
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
            BaseUsersSessionsService baseUsersSessionsService
    ) {
        super(
                securityJwtIncidentPublisher,
                usersEmailsService,
                baseUsersSessionsService
        );
    }

    @Override
    public void onAuthenticationLogin(EventAuthenticationLogin event) {
        super.onAuthenticationLogin(event);
        LOGGER.warn("[Server] SecurityJwtSubscriber.onAuthenticationLogin(). Username: `{}`", event.username());
    }
}
