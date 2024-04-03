package io.tech1.framework.b2b.base.security.jwt.domain.events;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentHeader;

public record EventAuthenticationLoginFailure(
        Username username,
        Password password,
        IPAddress ipAddress,
        UserAgentHeader userAgentHeader
) {

    public static EventAuthenticationLoginFailure testsHardcoded() {
        return new EventAuthenticationLoginFailure(
                Username.testsHardcoded(),
                Password.testsHardcoded(),
                IPAddress.testsHardcoded(),
                UserAgentHeader.testsHardcoded()
        );
    }
}
