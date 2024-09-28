package io.tech1.framework.iam.domain.functions;

import io.tech1.framework.iam.domain.db.UserSession;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.IPAddress;
import tech1.framework.foundation.domain.http.requests.UserAgentHeader;
import tech1.framework.foundation.domain.tuples.TupleToggle;
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
