package tech1.framework.foundation.incidents.domain.session;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import tech1.framework.foundation.incidents.domain.AbstractIncident;
import tech1.framework.foundation.incidents.domain.Incident;

public record IncidentSessionExpired(
        Username username,
        UserRequestMetadata userRequestMetadata
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        return new Incident(
                SecurityJwtIncidentType.SESSION_EXPIRED,
                this.username,
                this.userRequestMetadata
        );
    }
}
