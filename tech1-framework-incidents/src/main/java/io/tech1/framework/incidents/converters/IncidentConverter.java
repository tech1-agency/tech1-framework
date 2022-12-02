package io.tech1.framework.incidents.converters;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;

public interface IncidentConverter {
    Incident convert(IncidentThrowable incidentThrowable);
    Incident convert(IncidentAuthenticationLogin incidentAuthenticationLogin);
    Incident convert(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword);
    Incident convert(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword);
    Incident convert(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin);
    Incident convert(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull);
    Incident convert(IncidentSessionRefreshed incidentSessionRefreshed);
    Incident convert(IncidentSessionExpired incidentSessionExpired);
    Incident convert(IncidentRegistration1 incidentRegistration1);
    Incident convert(IncidentRegistration1Failure incidentRegistration1Failure);
}
