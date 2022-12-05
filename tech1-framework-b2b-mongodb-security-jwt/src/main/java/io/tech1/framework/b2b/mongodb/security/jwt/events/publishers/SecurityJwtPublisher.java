package io.tech1.framework.b2b.mongodb.security.jwt.events.publishers;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;
import org.springframework.scheduling.annotation.Async;

public interface SecurityJwtPublisher {
    @Async
    void publishAuthenticationLogin(EventAuthenticationLogin event);
    @Async
    void publishAuthenticationLoginFailure(EventAuthenticationLoginFailure event);
    @Async
    void publishAuthenticationLogout(EventAuthenticationLogout event);
    @Async
    void publishRegistration1(EventRegistration1 event);
    @Async
    void publishRegistration1Failure(EventRegistration1Failure event);
    @Async
    void publishSessionRefreshed(EventSessionRefreshed event);
    @Async
    void publishSessionExpired(EventSessionExpired event);
    @Async
    void publishSessionAddUserRequestMetadata(EventSessionAddUserRequestMetadata event);
}
