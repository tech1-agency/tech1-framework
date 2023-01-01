package io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters.SecurityJwtIncidentConverter;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.IncidentAttributes;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtIncidentConverterImpl implements SecurityJwtIncidentConverter {

    @Override
    public Incident convert(IncidentAuthenticationLogin incidentAuthenticationLogin) {
        return this.convertUserRequestMetadata(
                SecurityJwtIncidentType.AUTHENTICATION_LOGIN,
                incidentAuthenticationLogin.getUsername(),
                incidentAuthenticationLogin.getUserRequestMetadata()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword) {
        return this.convertAuthenticationLoginFailure(
                SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD,
                incidentAuthenticationLoginFailureUsernamePassword.getUsername(),
                incidentAuthenticationLoginFailureUsernamePassword.getPassword()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword) {
        return this.convertAuthenticationLoginFailure(
                SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD,
                incidentAuthenticationLoginFailureUsernameMaskedPassword.getUsername(),
                incidentAuthenticationLoginFailureUsernameMaskedPassword.getPassword()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin) {
        return this.convertOnlyUsername(SecurityJwtIncidentType.AUTHENTICATION_LOGOUT_MIN, incidentAuthenticationLogoutMin.getUsername());
    }

    @Override
    public Incident convert(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull) {
        return this.convertUserRequestMetadata(SecurityJwtIncidentType.AUTHENTICATION_LOGOUT, incidentAuthenticationLogoutFull.getUsername(), incidentAuthenticationLogoutFull.getUserRequestMetadata());
    }

    @Override
    public Incident convert(IncidentSessionRefreshed incidentSessionRefreshed) {
        return this.convertUserRequestMetadata(SecurityJwtIncidentType.SESSION_REFRESHED, incidentSessionRefreshed.getUsername(), incidentSessionRefreshed.getUserRequestMetadata());
    }

    @Override
    public Incident convert(IncidentSessionExpired incidentSessionExpired) {
        return this.convertUserRequestMetadata(SecurityJwtIncidentType.SESSION_EXPIRED, incidentSessionExpired.getUsername(), incidentSessionExpired.getUserRequestMetadata());
    }

    @Override
    public Incident convert(IncidentRegistration1 incidentRegistration1) {
        return this.convertOnlyUsername(SecurityJwtIncidentType.REGISTER1, incidentRegistration1.getUsername());
    }

    @Override
    public Incident convert(IncidentRegistration1Failure incidentRegistration1Failure) {
        var incident = this.convertOnlyUsername(SecurityJwtIncidentType.REGISTER1_FAILURE, incidentRegistration1Failure.getUsername());
        incident.add(IncidentAttributes.Keys.EXCEPTION, incidentRegistration1Failure.getException());
        incident.add(IncidentAttributes.Keys.INVITATION_CODE, incidentRegistration1Failure.getInvitationCode());
        return incident;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private Incident convertAuthenticationLoginFailure(SecurityJwtIncidentType incidentType, Username username, Password password) {
        var incident = this.convertOnlyUsername(incidentType, username);
        incident.add(IncidentAttributes.Keys.PASSWORD, password);
        return incident;
    }

    private Incident convertOnlyUsername(SecurityJwtIncidentType incidentType, Username username) {
        var incident = new Incident();
        incident.add(IncidentAttributes.Keys.TYPE, incidentType.toString());
        incident.add(IncidentAttributes.Keys.USERNAME, username);
        return incident;
    }

    private Incident convertUserRequestMetadata(SecurityJwtIncidentType incidentType, Username username, UserRequestMetadata userRequestMetadata) {
        var incident = this.convertOnlyUsername(incidentType, username);

        var tupleResponseException = userRequestMetadata.getException();
        if (!tupleResponseException.isOk()) {
            incident.add(IncidentAttributes.Keys.EXCEPTION, tupleResponseException.getMessage());
        }

        var whereTuple3 = userRequestMetadata.getWhereTuple3();
        incident.add(IncidentAttributes.Keys.IP_ADDRESS, whereTuple3.getA());
        incident.add(IncidentAttributes.Keys.COUNTRY_FLAG, whereTuple3.getB());
        incident.add(IncidentAttributes.Keys.WHERE, whereTuple3.getC());

        var whatTuple2 = userRequestMetadata.getWhatTuple2();
        incident.add(IncidentAttributes.Keys.BROWSER, whatTuple2.getA());
        incident.add(IncidentAttributes.Keys.WHAT, whatTuple2.getB());

        return incident;
    }
}
