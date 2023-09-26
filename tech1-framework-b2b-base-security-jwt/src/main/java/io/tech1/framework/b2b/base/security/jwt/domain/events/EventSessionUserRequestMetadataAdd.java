package io.tech1.framework.b2b.base.security.jwt.domain.events;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import org.jetbrains.annotations.NotNull;

public record EventSessionUserRequestMetadataAdd(
        @NotNull Username username,
        Email email,
        @NotNull UserSession session,
        @NotNull IPAddress clientIpAddr,
        UserAgentHeader userAgentHeader,
        boolean isAuthenticationLoginEndpoint,
        boolean isAuthenticationRefreshTokenEndpoint
) {
}
