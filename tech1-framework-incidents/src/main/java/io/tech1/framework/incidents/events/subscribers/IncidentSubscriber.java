package io.tech1.framework.incidents.events.subscribers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.Registration1FailureIncident;
import io.tech1.framework.incidents.domain.registration.Registration1Incident;
import io.tech1.framework.incidents.domain.session.SessionExpiredIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
import org.springframework.context.event.EventListener;

public interface IncidentSubscriber {
    @EventListener
    void onEvent(Incident incident);
    @EventListener
    void onEvent(ThrowableIncident throwableIncident);
    @EventListener
    void onEvent(AuthenticationLoginIncident authenticationLoginIncident);
    @EventListener
    void onEvent(AuthenticationLoginFailureUsernamePasswordIncident authenticationLoginFailureUsernamePasswordIncident);
    @EventListener
    void onEvent(AuthenticationLoginFailureUsernameMaskedPasswordIncident authenticationLoginFailureUsernameMaskedPasswordIncident);
    @EventListener
    void onEvent(AuthenticationLogoutMinIncident authenticationLogoutMinIncident);
    @EventListener
    void onEvent(AuthenticationLogoutFullIncident authenticationLogoutFullIncident);
    @EventListener
    void onEvent(SessionRefreshedIncident sessionRefreshedIncident);
    @EventListener
    void onEvent(SessionExpiredIncident sessionExpiredIncident);
    @EventListener
    void onEvent(Registration1Incident registration1Incident);
    @EventListener
    void onEvent(Registration1FailureIncident registration1FailureIncident);
}
