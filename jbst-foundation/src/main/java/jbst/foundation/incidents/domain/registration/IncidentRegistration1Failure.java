package jbst.foundation.incidents.domain.registration;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.base.SecurityJwtIncidentType;
import jbst.foundation.incidents.domain.AbstractIncident;
import jbst.foundation.incidents.domain.Incident;

import static jbst.foundation.incidents.domain.IncidentAttributes.Keys.*;

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
