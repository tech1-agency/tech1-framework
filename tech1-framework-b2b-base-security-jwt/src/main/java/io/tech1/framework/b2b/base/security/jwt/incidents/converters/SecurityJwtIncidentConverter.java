package io.tech1.framework.b2b.base.security.jwt.incidents.converters;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogin;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernameMaskedPassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernamePassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;

public interface SecurityJwtIncidentConverter {
    Incident convert(IncidentAuthenticationLogin incidentAuthenticationLogin);
    Incident convert(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword);
    Incident convert(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword);
    Incident convert(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull);
    Incident convert(IncidentSessionRefreshed incidentSessionRefreshed);
    Incident convert(IncidentSessionExpired incidentSessionExpired);
    Incident convert(IncidentRegistration1 incidentRegistration1);
    Incident convert(IncidentRegistration1Failure incidentRegistration1Failure);
}
