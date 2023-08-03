package io.tech1.framework.b2b.base.security.jwt.domain.events;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import org.jetbrains.annotations.NotNull;

public record EventSessionAddUserRequestMetadata(
        @NotNull Username username,
        Email email,
        @NotNull AnyDbUserSession session,
        @NotNull IPAddress clientIpAddr,
        UserAgentHeader userAgentHeader,
        boolean isAuthenticationLoginEndpoint,
        boolean isAuthenticationRefreshTokenEndpoint
) {

}
