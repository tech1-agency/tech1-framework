package io.tech1.framework.b2b.mongodb.security.jwt.events.publishers;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;
import org.springframework.scheduling.annotation.Async;

public interface SecurityJwtPublisher {
    @Async
    void publishAuthenticationLogin(EventAuthenticationLogin event);
    @Async
    void publishAuthenticationLoginFailureUsernamePassword(EventAuthenticationLoginFailureUsernamePassword event);
    @Async
    void publishAuthenticationLoginFailureUsernameMaskedPassword(EventAuthenticationLoginFailureUsernameMaskedPassword event);
    @Async
    void publishAuthenticationLogout(EventAuthenticationLogout event);
    @Async
    void publishRegistrationRegister1(EventRegistrationRegister1 event);
    @Async
    void publishSessionRefreshed(EventSessionRefreshed event);
    @Async
    void publishSessionExpired(EventSessionExpired event);
    @Async
    void publishSessionAddUserRequestMetadata(EventSessionAddUserRequestMetadata event);
}
