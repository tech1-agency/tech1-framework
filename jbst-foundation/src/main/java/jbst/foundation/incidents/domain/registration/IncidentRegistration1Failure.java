package jbst.foundation.incidents.domain.registration;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.base.SecurityJwtIncidentType;
import jbst.foundation.incidents.domain.AbstractIncident;
import jbst.foundation.incidents.domain.Incident;

import static jbst.foundation.incidents.domain.IncidentAttributes.Keys.*;

public record IncidentRegistration1Failure(
        Username username,
        String invitation,
        Username invitationOwner,
        String exception
) implements AbstractIncident {

    public static IncidentRegistration1Failure of(
            Username username,
            String invitation,
            String exception
    ) {
        return new IncidentRegistration1Failure(
                username,
                invitation,
                Username.dash(),
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
        incident.add(INVITATION, this.invitation);
        incident.add(INVITATION_OWNER, this.invitationOwner);
        return incident;
    }
}
