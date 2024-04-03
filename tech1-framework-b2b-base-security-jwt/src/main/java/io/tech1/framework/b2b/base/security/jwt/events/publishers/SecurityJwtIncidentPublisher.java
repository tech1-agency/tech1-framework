package io.tech1.framework.b2b.base.security.jwt.events.publishers;

import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;

public interface SecurityJwtIncidentPublisher {
    void publishAuthenticationLogin(IncidentAuthenticationLogin incident);
    // TODO [YYL-clean]
    void publishAuthenticationLoginFailureUsernamePassword(IncidentAuthenticationLoginFailureUsernamePassword incident);
    // TODO [YYL-clean]
    void publishAuthenticationLoginFailureUsernameMaskedPassword(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident);
    void publishAuthenticationLogoutMin(IncidentAuthenticationLogoutMin incident);
    void publishAuthenticationLogoutFull(IncidentAuthenticationLogoutFull incident);
    void publishRegistration1(IncidentRegistration1 incident);
    void publishRegistration1Failure(IncidentRegistration1Failure incident);
    void publishSessionRefreshed(IncidentSessionRefreshed incident);
    void publishSessionExpired(IncidentSessionExpired incident);
}
