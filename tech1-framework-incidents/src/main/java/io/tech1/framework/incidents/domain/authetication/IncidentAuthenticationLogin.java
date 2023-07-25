package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

public record IncidentAuthenticationLogin(
        Username username,
        UserRequestMetadata userRequestMetadata
) {
}
