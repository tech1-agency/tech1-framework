package io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters.SecurityJwtIncidentConverter;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogin;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;
import static io.tech1.framework.incidents.domain.IncidentAttributes.Keys.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtIncidentConverterImpl implements SecurityJwtIncidentConverter {

    private final IncidentConverter incidentConverter;

    @Override
    public Incident convert(IncidentAuthenticationLogin incidentAuthenticationLogin) {
        return this.convertUserRequestMetadata(
                AUTHENTICATION_LOGIN,
                incidentAuthenticationLogin.getUsername(),
                incidentAuthenticationLogin.getUserRequestMetadata()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull) {
        return this.convertUserRequestMetadata(
                AUTHENTICATION_LOGOUT,
                incidentAuthenticationLogoutFull.getUsername(),
                incidentAuthenticationLogoutFull.getUserRequestMetadata()
        );
    }

    @Override
    public Incident convert(IncidentSessionRefreshed incidentSessionRefreshed) {
        return this.convertUserRequestMetadata(
                SESSION_REFRESHED,
                incidentSessionRefreshed.getUsername(),
                incidentSessionRefreshed.getUserRequestMetadata()
        );
    }

    @Override
    public Incident convert(IncidentSessionExpired incidentSessionExpired) {
        return this.convertUserRequestMetadata(
                SESSION_EXPIRED,
                incidentSessionExpired.getUsername(),
                incidentSessionExpired.getUserRequestMetadata()
        );
    }

    @Override
    public Incident convert(IncidentRegistration1 incidentRegistration1) {
        return this.incidentConverter.convertUsername(
                REGISTER1.toString(),
                incidentRegistration1.getUsername()
        );
    }

    @Override
    public Incident convert(IncidentRegistration1Failure incidentRegistration1Failure) {
        var incident = this.incidentConverter.convertUsername(
                REGISTER1_FAILURE.toString(),
                incidentRegistration1Failure.getUsername()
        );
        incident.add(EXCEPTION, incidentRegistration1Failure.getException());
        incident.add(INVITATION_CODE, incidentRegistration1Failure.getInvitationCode());
        incident.add(INVITATION_CODE_OWNER, incidentRegistration1Failure.getInvitationCodeOwner());
        return incident;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private Incident convertUserRequestMetadata(SecurityJwtIncidentType incidentType, Username username, UserRequestMetadata userRequestMetadata) {
        var incident = this.incidentConverter.convertUsername(incidentType.toString(), username);

        var tupleResponseException = userRequestMetadata.getException();
        if (!tupleResponseException.isOk()) {
            incident.add(EXCEPTION, tupleResponseException.getMessage());
        }

        var whereTuple3 = userRequestMetadata.getWhereTuple3();
        incident.add(IP_ADDRESS, whereTuple3.a());
        incident.add(COUNTRY_FLAG, whereTuple3.b());
        incident.add(WHERE, whereTuple3.c());

        var whatTuple2 = userRequestMetadata.getWhatTuple2();
        incident.add(BROWSER, whatTuple2.a());
        incident.add(WHAT, whatTuple2.b());

        return incident;
    }
}
