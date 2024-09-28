package tech1.framework.foundation.incidents.domain.registration;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import tech1.framework.foundation.incidents.domain.AbstractIncident;
import tech1.framework.foundation.incidents.domain.Incident;

import static tech1.framework.foundation.incidents.domain.IncidentAttributes.Keys.*;

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
