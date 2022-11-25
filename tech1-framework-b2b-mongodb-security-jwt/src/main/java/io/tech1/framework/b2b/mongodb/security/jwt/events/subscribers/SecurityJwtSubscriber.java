package io.tech1.framework.b2b.mongodb.security.jwt.events.subscribers;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;
import org.springframework.context.event.EventListener;

public interface SecurityJwtSubscriber {
    @EventListener
    void onAuthenticationLogin(EventAuthenticationLogin event);
    @EventListener
    void onAuthenticationLoginFailureUsernamePassword(EventAuthenticationLoginFailureUsernamePassword event);
    @EventListener
    void onAuthenticationLoginFailureUsernameMaskedPassword(EventAuthenticationLoginFailureUsernameMaskedPassword event);
    @EventListener
    void onAuthenticationLogout(EventAuthenticationLogout event);
    @EventListener
    void onRegistrationRegister1(EventRegistrationRegister1 event);
    @EventListener
    void onSessionRefreshed(EventSessionRefreshed event);
    @EventListener
    void onSessionExpired(EventSessionExpired event);
    @EventListener
    void onSessionAddUserRequestMetadata(EventSessionAddUserRequestMetadata event);
}
