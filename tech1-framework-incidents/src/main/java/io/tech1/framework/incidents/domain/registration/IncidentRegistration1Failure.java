package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.incidents.domain.AbstractIncident;
import io.tech1.framework.incidents.domain.Incident;

import static io.tech1.framework.incidents.domain.IncidentAttributes.Keys.*;

public record IncidentRegistration1Failure(
        Username username,
        String invitationCode,
        Username invitationCodeOwner,
        String exception
) implements AbstractIncident {

    public static IncidentRegistration1Failure of(
            Username username,
            String invitationCode,
            String exception
    ) {
        return new IncidentRegistration1Failure(
                username,
                invitationCode,
                Username.of("—"),
                exception
        );
    }

    @Override
    public Incident getPlainIncident() {
        var incident = new Incident(
                SecurityJwtIncidentType.REGISTER1_FAILURE,
                this.username
        );
        incident.add(EXCEPTION, this.exception);
        incident.add(INVITATION_CODE, this.invitationCode);
        incident.add(INVITATION_CODE_OWNER, this.invitationCodeOwner);
        return incident;
    }
}
