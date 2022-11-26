package io.tech1.framework.incidents.events.publishers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.Register1FailureIncident;
import io.tech1.framework.incidents.domain.registration.Register1Incident;
import io.tech1.framework.incidents.domain.session.SessionExpiredIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
import org.springframework.scheduling.annotation.Async;

public interface IncidentPublisher {
    @Async
    void publishIncident(Incident incident);
    @Async
    void publishThrowable(ThrowableIncident incident);
    @Async
    void publishThrowable(Throwable throwable);
    @Async
    void publishAuthenticationLogin(AuthenticationLoginIncident incident);
    @Async
    void publishAuthenticationLoginFailureUsernamePassword(AuthenticationLoginFailureUsernamePasswordIncident incident);
    @Async
    void publishAuthenticationLoginFailureUsernameMaskedPassword(AuthenticationLoginFailureUsernameMaskedPasswordIncident incident);
    @Async
    void publishAuthenticationLogoutMin(AuthenticationLogoutMinIncident incident);
    @Async
    void publishAuthenticationLogoutFull(AuthenticationLogoutFullIncident incident);
    @Async
    void publishSessionRefreshed(SessionRefreshedIncident incident);
    @Async
    void publishSessionExpired(SessionExpiredIncident incident);
    @Async
    void publishRegistration1(Register1Incident incident);
    @Async
    void publishRegistration1Failure(Register1FailureIncident incident);
}
