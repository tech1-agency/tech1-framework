package io.tech1.framework.b2b.base.security.jwt.events.subscribers;

import io.tech1.framework.b2b.base.security.jwt.domain.events.*;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface SecurityJwtSubscriber {
    @Async
    @EventListener
    void onAuthenticationLogin(EventAuthenticationLogin event);
    @Async
    @EventListener
    void onAuthenticationLoginFailure(EventAuthenticationLoginFailure event);
    @Async
    @EventListener
    void onAuthenticationLogout(EventAuthenticationLogout event);
    @Async
    @EventListener
    void onRegistration1(EventRegistration1 event);
    @Async
    @EventListener
    void onRegistration1Failure(EventRegistration1Failure event);
    @Async
    @EventListener
    void onSessionRefreshed(EventSessionRefreshed event);
    @Async
    @EventListener
    void onSessionExpired(EventSessionExpired event);
    @Async
    @EventListener
    void onSessionUserRequestMetadataAdd(EventSessionUserRequestMetadataAdd event);
    @Async
    @EventListener
    void onSessionUserRequestMetadataRenew(EventSessionUserRequestMetadataRenew event);
    @Async
    @EventListener
    void onSessionUserRequestMetadataRenewCron(EventSessionUserRequestMetadataRenewCron event);
    @Async
    @EventListener
    void onSessionUserRequestMetadataRenewManually(EventSessionUserRequestMetadataRenewManually event);
}
