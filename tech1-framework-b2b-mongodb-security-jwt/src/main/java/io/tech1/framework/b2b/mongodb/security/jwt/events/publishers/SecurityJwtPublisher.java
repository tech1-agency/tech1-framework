package io.tech1.framework.b2b.mongodb.security.jwt.events.publishers;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.*;

public interface SecurityJwtPublisher {
    void publishAuthenticationLogin(EventAuthenticationLogin event);
    void publishAuthenticationLoginFailure(EventAuthenticationLoginFailure event);
    void publishAuthenticationLogout(EventAuthenticationLogout event);
    void publishRegistration1(EventRegistration1 event);
    void publishRegistration1Failure(EventRegistration1Failure event);
    void publishSessionRefreshed(EventSessionRefreshed event);
    void publishSessionExpired(EventSessionExpired event);
    void publishSessionAddUserRequestMetadata(EventSessionAddUserRequestMetadata event);
}
