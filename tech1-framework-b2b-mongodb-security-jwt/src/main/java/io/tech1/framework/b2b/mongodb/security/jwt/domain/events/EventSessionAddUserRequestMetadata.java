package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import org.jetbrains.annotations.NotNull;

public record EventSessionAddUserRequestMetadata(
        @NotNull Username username,
        Email email,
        @NotNull UserSessionId userSessionId,
        @NotNull IPAddress clientIpAddr,
        UserAgentHeader userAgentHeader,
        boolean isAuthenticationLoginEndpoint,
        boolean isAuthenticationRefreshTokenEndpoint
) {

}
