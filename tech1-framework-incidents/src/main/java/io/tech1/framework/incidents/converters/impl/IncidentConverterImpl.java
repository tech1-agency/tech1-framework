package io.tech1.framework.incidents.converters.impl;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.IncidentAttributes;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.Register1FailureIncident;
import io.tech1.framework.incidents.domain.registration.Register1Incident;
import io.tech1.framework.incidents.domain.session.SessionExpiredIncident;
import io.tech1.framework.incidents.domain.session.SessionRefreshedIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static io.tech1.framework.domain.utilities.exceptions.ThrowableUtility.getTrace;
import static io.tech1.framework.incidents.domain.IncidentAttributes.Values.*;
import static java.util.Objects.nonNull;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentConverterImpl implements IncidentConverter {

    @Override
    public Incident convert(ThrowableIncident throwableIncident) {
        var incident = new Incident();
        incident.add(IncidentAttributes.Keys.TYPE, IncidentAttributes.Values.THROWABLE);

        var throwable = throwableIncident.getThrowable();
        incident.add(IncidentAttributes.Keys.EXCEPTION, throwable.getClass());
        incident.add(IncidentAttributes.Keys.TRACE, getTrace(throwable));
        incident.add(IncidentAttributes.Keys.MESSAGE, throwable.getMessage());

        var method = throwableIncident.getMethod();
        if (nonNull(method)) {
            incident.add(IncidentAttributes.Keys.METHOD, method.toString());
        }

        var params = throwableIncident.getParams();
        if (nonNull(params) && params.size() > 0) {
            incident.add(IncidentAttributes.Keys.PARAMS, params.stream().map(Object::toString).collect(Collectors.joining(", ")));
        }

        var attributes = throwableIncident.getAttributes();
        if (nonNull(attributes) && attributes.size() > 0) {
            attributes.forEach(incident::add);
        }
        return incident;
    }

    @Override
    public Incident convert(AuthenticationLoginIncident authenticationLoginIncident) {
        return this.convertUserRequestMetadata(AUTHENTICATION_LOGIN, authenticationLoginIncident.getUsername(), authenticationLoginIncident.getUserRequestMetadata());
    }

    @Override
    public Incident convert(AuthenticationLoginFailureUsernamePasswordIncident authenticationLoginFailureUsernamePasswordIncident) {
        return this.convertAuthenticationLoginFailure(
                authenticationLoginFailureUsernamePasswordIncident.getUsername(),
                authenticationLoginFailureUsernamePasswordIncident.getPassword()
        );
    }

    @Override
    public Incident convert(AuthenticationLoginFailureUsernameMaskedPasswordIncident authenticationLoginFailureUsernameMaskedPasswordIncident) {
        return this.convertAuthenticationLoginFailure(
                authenticationLoginFailureUsernameMaskedPasswordIncident.getUsername(),
                authenticationLoginFailureUsernameMaskedPasswordIncident.getPassword()
        );
    }

    @Override
    public Incident convert(AuthenticationLogoutMinIncident authenticationLogoutMinIncident) {
        return this.convertOnlyUsername(AUTHENTICATION_LOGOUT, authenticationLogoutMinIncident.getUsername());
    }

    @Override
    public Incident convert(AuthenticationLogoutFullIncident authenticationLogoutFullIncident) {
        return this.convertUserRequestMetadata(AUTHENTICATION_LOGOUT, authenticationLogoutFullIncident.getUsername(), authenticationLogoutFullIncident.getUserRequestMetadata());
    }

    @Override
    public Incident convert(SessionRefreshedIncident sessionRefreshedIncident) {
        return this.convertUserRequestMetadata(SESSION_REFRESHED, sessionRefreshedIncident.getUsername(), sessionRefreshedIncident.getUserRequestMetadata());
    }

    @Override
    public Incident convert(SessionExpiredIncident sessionExpiredIncident) {
        return this.convertUserRequestMetadata(SESSION_EXPIRED, sessionExpiredIncident.getUsername(), sessionExpiredIncident.getUserRequestMetadata());
    }

    @Override
    public Incident convert(Register1Incident register1Incident) {
        return this.convertOnlyUsername(REGISTER1, register1Incident.getUsername());
    }

    @Override
    public Incident convert(Register1FailureIncident register1FailureIncident) {
        var incident = this.convertOnlyUsername(REGISTER1_FAILURE, register1FailureIncident.getUsername());
        incident.add(IncidentAttributes.Keys.EXCEPTION, register1FailureIncident.getException());
        incident.add(IncidentAttributes.Keys.INVITATION_CODE, register1FailureIncident.getInvitationCode());
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
