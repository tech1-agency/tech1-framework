package io.tech1.framework.iam.domain.events;

import io.tech1.framework.foundation.domain.base.Password;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import io.tech1.framework.foundation.domain.http.requests.UserAgentHeader;

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
