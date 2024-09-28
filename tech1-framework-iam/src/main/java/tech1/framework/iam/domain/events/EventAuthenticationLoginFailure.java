package tech1.framework.iam.domain.events;

import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.IPAddress;
import tech1.framework.foundation.domain.http.requests.UserAgentHeader;

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
