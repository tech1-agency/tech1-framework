package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentHeader;

import javax.servlet.http.HttpServletRequest;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

public record EventSessionAddUserRequestMetadata(
        Username username,
        Email email,
        MongoDbUserSession userSession,
        IPAddress clientIpAddr,
        UserAgentHeader userAgentHeader,
        boolean isAuthenticationLoginEndpoint,
        boolean isAuthenticationRefreshTokenEndpoint
) {
    public static EventSessionAddUserRequestMetadata of(
            Username username,
            Email email,
            MongoDbUserSession userSession,
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
                email,
                userSession,
                clientIpAddr,
                new UserAgentHeader(httpServletRequest),
                isAuthenticationLoginEndpoint,
                isAuthenticationRefreshTokenEndpoint
        );
    }
}
