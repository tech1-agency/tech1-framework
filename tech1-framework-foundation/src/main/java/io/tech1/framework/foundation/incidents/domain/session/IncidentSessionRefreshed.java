package io.tech1.framework.foundation.incidents.domain.session;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.foundation.incidents.domain.AbstractIncident;
import io.tech1.framework.foundation.incidents.domain.Incident;

public record IncidentSessionRefreshed(
        Username username,
        UserRequestMetadata userRequestMetadata
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        return new Incident(
                SecurityJwtIncidentType.SESSION_REFRESHED,
                this.username,
                this.userRequestMetadata
        );
    }
}
