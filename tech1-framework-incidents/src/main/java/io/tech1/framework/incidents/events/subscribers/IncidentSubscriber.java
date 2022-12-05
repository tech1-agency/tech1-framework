package io.tech1.framework.incidents.events.subscribers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import org.springframework.context.event.EventListener;

public interface IncidentSubscriber {
    @EventListener
    void onEvent(Incident incident);
    @EventListener
    void onEvent(IncidentThrowable incidentThrowable);
    @EventListener
    void onEvent(IncidentAuthenticationLogin incidentAuthenticationLogin);
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword);
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword);
    @EventListener
    void onEvent(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin);
    @EventListener
    void onEvent(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull);
    @EventListener
    void onEvent(IncidentRegistration1 incidentRegistration1);
    @EventListener
    void onEvent(IncidentRegistration1Failure incidentRegistration1Failure);
    @EventListener
    void onEvent(IncidentSessionRefreshed incidentSessionRefreshed);
    @EventListener
    void onEvent(IncidentSessionExpired incidentSessionExpired);
}
