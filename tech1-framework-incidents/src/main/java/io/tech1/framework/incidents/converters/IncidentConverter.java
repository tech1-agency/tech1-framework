package io.tech1.framework.incidents.converters;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.Register1FailureIncident;
import io.tech1.framework.incidents.domain.registration.Register1Incident;
import io.tech1.framework.incidents.domain.session.SessionExpiredIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;

public interface IncidentConverter {
    Incident convert(ThrowableIncident throwableIncident);
    Incident convert(AuthenticationLoginIncident authenticationLoginIncident);
    Incident convert(AuthenticationLoginFailureUsernamePasswordIncident authenticationLoginFailureUsernamePasswordIncident);
    Incident convert(AuthenticationLoginFailureUsernameMaskedPasswordIncident authenticationLoginFailureUsernameMaskedPasswordIncident);
    Incident convert(AuthenticationLogoutMinIncident authenticationLogoutMinIncident);
    Incident convert(AuthenticationLogoutFullIncident authenticationLogoutFullIncident);
    Incident convert(SessionRefreshedIncident sessionRefreshedIncident);
    Incident convert(SessionExpiredIncident sessionExpiredIncident);
    Incident convert(Register1Incident register1Incident);
    Incident convert(Register1FailureIncident register1FailureIncident);
}
