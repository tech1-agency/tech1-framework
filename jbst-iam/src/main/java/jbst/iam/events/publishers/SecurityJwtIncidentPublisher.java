package jbst.iam.events.publishers;

import jbst.foundation.incidents.domain.authetication.*;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import jbst.foundation.incidents.domain.session.IncidentSessionExpired;
import jbst.foundation.incidents.domain.session.IncidentSessionRefreshed;

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
