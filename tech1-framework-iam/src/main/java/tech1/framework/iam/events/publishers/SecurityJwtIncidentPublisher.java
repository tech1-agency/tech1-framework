package tech1.framework.iam.events.publishers;

import tech1.framework.foundation.incidents.domain.authetication.*;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionExpired;
import tech1.framework.foundation.incidents.domain.session.IncidentSessionRefreshed;

public interface SecurityJwtIncidentPublisher {
    void publishAuthenticationLogin(IncidentAuthenticationLogin incident);
    void publishAuthenticationLoginFailureUsernamePassword(IncidentAuthenticationLoginFailureUsernamePassword incident);
    void publishAuthenticationLoginFailureUsernameMaskedPassword(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident);
    void publishAuthenticationLogoutMin(IncidentAuthenticationLogoutMin incident);
    void publishAuthenticationLogoutFull(IncidentAuthenticationLogoutFull incident);
    void publishRegistration1(IncidentRegistration1 incident);
    void publishRegistration1Failure(IncidentRegistration1Failure incident);
    void publishSessionRefreshed(IncidentSessionRefreshed incident);
    void publishSessionExpired(IncidentSessionExpired incident);
}
