package io.tech1.framework.b2b.base.security.jwt.events.subscribers;

import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface SecurityJwtIncidentSubscriber {
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLogin incident);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernamePassword incident);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLogoutMin incident);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLogoutFull incident);
    @Async
    @EventListener
    void onEvent(IncidentRegistration1 incident);
    @Async
    @EventListener
    void onEvent(IncidentRegistration1Failure incident);
    @Async
    @EventListener
    void onEvent(IncidentSessionRefreshed incident);
    @Async
    @EventListener
    void onEvent(IncidentSessionExpired incident);
}
