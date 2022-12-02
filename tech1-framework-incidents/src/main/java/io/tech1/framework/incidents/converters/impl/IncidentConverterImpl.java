package io.tech1.framework.incidents.converters.impl;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.IncidentAttributes;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static io.tech1.framework.domain.utilities.exceptions.ThrowableUtility.getTrace;
import static io.tech1.framework.incidents.domain.IncidentAttributes.Values.*;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentConverterImpl implements IncidentConverter {

    @Override
    public Incident convert(IncidentThrowable incidentThrowable) {
        var incident = new Incident();
        incident.add(IncidentAttributes.Keys.TYPE, IncidentAttributes.Values.THROWABLE);

        var throwable = incidentThrowable.getThrowable();
        incident.add(IncidentAttributes.Keys.EXCEPTION, throwable.getClass());
        incident.add(IncidentAttributes.Keys.TRACE, getTrace(throwable));
        incident.add(IncidentAttributes.Keys.MESSAGE, throwable.getMessage());

        var method = incidentThrowable.getMethod();
        if (nonNull(method)) {
            incident.add(IncidentAttributes.Keys.METHOD, method.toString());
        }

        var params = incidentThrowable.getParams();
        if (!isEmpty(params)) {
            incident.add(IncidentAttributes.Keys.PARAMS, params.stream().map(Object::toString).collect(Collectors.joining(", ")));
        }

        var attributes = incidentThrowable.getAttributes();
        if (!isEmpty(attributes)) {
            attributes.forEach(incident::add);
        }
        return incident;
    }

    @Override
    public Incident convert(IncidentAuthenticationLogin incidentAuthenticationLogin) {
        return this.convertUserRequestMetadata(AUTHENTICATION_LOGIN, incidentAuthenticationLogin.getUsername(), incidentAuthenticationLogin.getUserRequestMetadata());
    }

    @Override
    public Incident convert(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword) {
        return this.convertAuthenticationLoginFailure(
                incidentAuthenticationLoginFailureUsernamePassword.getUsername(),
                incidentAuthenticationLoginFailureUsernamePassword.getPassword()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword) {
        return this.convertAuthenticationLoginFailure(
                incidentAuthenticationLoginFailureUsernameMaskedPassword.getUsername(),
                incidentAuthenticationLoginFailureUsernameMaskedPassword.getPassword()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin) {
        return this.convertOnlyUsername(AUTHENTICATION_LOGOUT, incidentAuthenticationLogoutMin.getUsername());
    }

    @Override
    public Incident convert(IncidentAuthenticationLogoutFull incidentAuthenticationLogoutFull) {
        return this.convertUserRequestMetadata(AUTHENTICATION_LOGOUT, incidentAuthenticationLogoutFull.getUsername(), incidentAuthenticationLogoutFull.getUserRequestMetadata());
    }

    @Override
    public Incident convert(IncidentSessionRefreshed incidentSessionRefreshed) {
        return this.convertUserRequestMetadata(SESSION_REFRESHED, incidentSessionRefreshed.getUsername(), incidentSessionRefreshed.getUserRequestMetadata());
    }

    @Override
    public Incident convert(IncidentSessionExpired incidentSessionExpired) {
        return this.convertUserRequestMetadata(SESSION_EXPIRED, incidentSessionExpired.getUsername(), incidentSessionExpired.getUserRequestMetadata());
    }

    @Override
    public Incident convert(IncidentRegistration1 incidentRegistration1) {
        return this.convertOnlyUsername(REGISTER1, incidentRegistration1.getUsername());
    }

    @Override
    public Incident convert(IncidentRegistration1Failure incidentRegistration1Failure) {
        var incident = this.convertOnlyUsername(REGISTER1_FAILURE, incidentRegistration1Failure.getUsername());
        incident.add(IncidentAttributes.Keys.EXCEPTION, incidentRegistration1Failure.getException());
        incident.add(IncidentAttributes.Keys.INVITATION_CODE, incidentRegistration1Failure.getInvitationCode());
        return incident;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private Incident convertAuthenticationLoginFailure(Username username, Password password) {
        var incident = this.convertOnlyUsername(AUTHENTICATION_LOGIN_FAILURE, username);
        incident.add(IncidentAttributes.Keys.PASSWORD, password);
        return incident;
    }

    private Incident convertOnlyUsername(String incidentType, Username username) {
        var incident = new Incident();
        incident.add(IncidentAttributes.Keys.TYPE, incidentType);
        incident.add(IncidentAttributes.Keys.USERNAME, username);
        return incident;
    }

    private Incident convertUserRequestMetadata(String incidentType, Username username, UserRequestMetadata userRequestMetadata) {
        var incident = this.convertOnlyUsername(incidentType, username);

        var tupleResponseException = userRequestMetadata.getException();
        if (!tupleResponseException.isOk()) {
            incident.add(IncidentAttributes.Keys.EXCEPTION, tupleResponseException.getMessage());
        }

        var whereTuple3 = userRequestMetadata.getWhereTuple3();
        incident.add(IncidentAttributes.Keys.IP_ADDRESS, whereTuple3.getA());
        incident.add(IncidentAttributes.Keys.COUNTRY, whereTuple3.getB());
        incident.add(IncidentAttributes.Keys.WHERE, whereTuple3.getC());

        var whatTuple2 = userRequestMetadata.getWhatTuple2();
        incident.add(IncidentAttributes.Keys.BROWSER, whatTuple2.getA());
        incident.add(IncidentAttributes.Keys.WHAT, whatTuple2.getB());

        return incident;
    }
}
