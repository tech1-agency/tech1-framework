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
    void onEvent(IncidentAuthenticationLogin incidentAuthenticationLogin);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin);
    @Async
    @EventListener
    void onEvent(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull);
    @Async
    @EventListener
    void onEvent(IncidentRegistration1 incidentRegistration1);
    @Async
    @EventListener
    void onEvent(IncidentRegistration1Failure incidentRegistration1Failure);
    @Async
    @EventListener
    void onEvent(IncidentSessionRefreshed incidentSessionRefreshed);
    @Async
    @EventListener
    void onEvent(IncidentSessionExpired incidentSessionExpired);
}
