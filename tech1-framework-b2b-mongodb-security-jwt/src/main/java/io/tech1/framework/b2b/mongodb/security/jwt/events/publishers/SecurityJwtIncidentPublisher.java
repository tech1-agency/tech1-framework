package io.tech1.framework.b2b.mongodb.security.jwt.events.publishers;

import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import org.springframework.scheduling.annotation.Async;

public interface SecurityJwtIncidentPublisher {
    @Async
    void publishAuthenticationLogin(IncidentAuthenticationLogin incident);
    @Async
    void publishAuthenticationLoginFailureUsernamePassword(IncidentAuthenticationLoginFailureUsernamePassword incident);
    @Async
    void publishAuthenticationLoginFailureUsernameMaskedPassword(IncidentAuthenticationLoginFailureUsernameMaskedPassword incident);
    @Async
    void publishAuthenticationLogoutMin(IncidentAuthenticationLogoutMin incident);
    @Async
    void publishAuthenticationLogoutFull(IncidentAuthenticationLogoutFull incident);
    @Async
    void publishRegistration1(IncidentRegistration1 incident);
    @Async
    void publishRegistration1Failure(IncidentRegistration1Failure incident);
    @Async
    void publishSessionRefreshed(IncidentSessionRefreshed incident);
    @Async
    void publishSessionExpired(IncidentSessionExpired incident);
}
