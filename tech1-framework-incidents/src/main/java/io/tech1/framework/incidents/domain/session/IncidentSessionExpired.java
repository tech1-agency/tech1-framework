package io.tech1.framework.incidents.domain.session;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

public record IncidentSessionExpired(
        Username username,
        UserRequestMetadata userRequestMetadata
) {
}
