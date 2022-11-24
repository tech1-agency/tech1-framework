package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Data
public class EventSessionAddUserRequestMetadata {
    private final Username username;
    private final DbUserSession userSession;
    private final IPAddress clientIpAddr;
    private final UserAgentHeader userAgentHeader;
    private final boolean isAuthenticationLoginEndpoint;
    private final boolean isAuthenticationRefreshTokenEndpoint;

    public static EventSessionAddUserRequestMetadata of(
            Username username,
            DbUserSession userSession,
            IPAddress clientIpAddr,
            HttpServletRequest httpServletRequest,
            boolean isAuthenticationLoginEndpoint,
            boolean isAuthenticationRefreshTokenEndpoint
    ) {
        assertNonNullOrThrow(username, invalidAttribute("EventSessionAddUserRequestMetadata.username"));
        assertNonNullOrThrow(userSession, invalidAttribute("EventSessionAddUserRequestMetadata.userSession"));
        assertNonNullOrThrow(clientIpAddr, invalidAttribute("EventSessionAddUserRequestMetadata.clientIpAddr"));
        assertNonNullOrThrow(httpServletRequest, invalidAttribute("EventSessionAddUserRequestMetadata.httpServletRequest"));
        return new EventSessionAddUserRequestMetadata(
                username,
                userSession,
                clientIpAddr,
                new UserAgentHeader(httpServletRequest),
                isAuthenticationLoginEndpoint,
                isAuthenticationRefreshTokenEndpoint
        );
    }
}
