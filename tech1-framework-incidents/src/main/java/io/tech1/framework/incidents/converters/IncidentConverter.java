package io.tech1.framework.incidents.converters;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernameMaskedPassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernamePassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;

public interface IncidentConverter {
    Incident convert(IncidentThrowable incidentThrowable);
    Incident convert(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword);
    Incident convert(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword);
    Incident convert(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin);

    Incident convertUsername(String incidentType, Username username);
}
