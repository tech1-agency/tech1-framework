package jbst.iam.events.subscribers;

import jbst.foundation.incidents.domain.authetication.*;
import org.springframework.context.event.EventListener;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import jbst.foundation.incidents.domain.session.IncidentSessionExpired;
import jbst.foundation.incidents.domain.session.IncidentSessionRefreshed;

public interface SecurityJwtIncidentSubscriber {
    @EventListener
    void onEvent(IncidentAuthenticationLogin incident);
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernamePassword incident);
    @EventListener
    void onEvent(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident);
    @EventListener
    void onEvent(IncidentAuthenticationLogoutMin incident);
    @EventListener
    void onEvent(IncidentAuthenticationLogoutFull incident);
    @EventListener
    void onEvent(IncidentRegistration1 incident);
    @EventListener
    void onEvent(IncidentRegistration1Failure incident);
    @EventListener
    void onEvent(IncidentSessionRefreshed incident);
    @EventListener
    void onEvent(IncidentSessionExpired incident);
}
