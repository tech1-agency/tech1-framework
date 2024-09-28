package tech1.framework.iam.events.subscribers;

import org.springframework.context.event.EventListener;
import tech1.framework.iam.domain.events.*;

public interface SecurityJwtSubscriber {
    @EventListener
    void onAuthenticationLogin(EventAuthenticationLogin event);
    @EventListener
    void onAuthenticationLoginFailure(EventAuthenticationLoginFailure event);
    @EventListener
    void onAuthenticationLogout(EventAuthenticationLogout event);
    @EventListener
    void onRegistration1(EventRegistration1 event);
    @EventListener
    void onRegistration1Failure(EventRegistration1Failure event);
    @EventListener
    void onSessionRefreshed(EventSessionRefreshed event);
    @EventListener
    void onSessionExpired(EventSessionExpired event);
    @EventListener
    void onSessionUserRequestMetadataAdd(EventSessionUserRequestMetadataAdd event);
    @EventListener
    void onSessionUserRequestMetadataRenew(EventSessionUserRequestMetadataRenew event);
}
