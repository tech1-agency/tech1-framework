package io.tech1.framework.incidents.domain.session;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.Data;

// Lombok
@Data
public class IncidentSessionExpired {
    private final Username username;
    private final UserRequestMetadata userRequestMetadata;
}
