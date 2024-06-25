package io.tech1.framework.b2b.base.security.jwt.domain.functions;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import io.tech1.framework.foundation.domain.http.requests.UserAgentHeader;
import io.tech1.framework.foundation.domain.tuples.TupleToggle;
import org.jetbrains.annotations.NotNull;

public record FunctionSessionUserRequestMetadataSave(
        @NotNull Username username,
        @NotNull UserSession session,
        @NotNull IPAddress clientIpAddr,
        UserAgentHeader userAgentHeader,
        TupleToggle<Boolean> metadataRenewCron,
        TupleToggle<Boolean> metadataRenewManually
) {
}
