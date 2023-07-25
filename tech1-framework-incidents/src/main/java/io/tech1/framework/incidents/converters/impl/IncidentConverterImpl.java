package io.tech1.framework.incidents.converters.impl;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.IncidentAttributes;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernameMaskedPassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernamePassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;
import static io.tech1.framework.domain.utilities.exceptions.TraceUtility.getTrace;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentConverterImpl implements IncidentConverter {

    @Override
    public Incident convert(IncidentThrowable incidentThrowable) {
        var incident = new Incident();
        incident.add(IncidentAttributes.Keys.TYPE, IncidentAttributes.IncidentsTypes.THROWABLE);

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
    public Incident convert(IncidentAuthenticationLoginFailureUsernamePassword incidentAuthenticationLoginFailureUsernamePassword) {
        return this.convertAuthenticationLoginFailure(
                AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD.toString(),
                incidentAuthenticationLoginFailureUsernamePassword.username(),
                incidentAuthenticationLoginFailureUsernamePassword.password()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLoginFailureUsernameMaskedPassword incidentAuthenticationLoginFailureUsernameMaskedPassword) {
        return this.convertAuthenticationLoginFailure(
                AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD.toString(),
                incidentAuthenticationLoginFailureUsernameMaskedPassword.username(),
                incidentAuthenticationLoginFailureUsernameMaskedPassword.password()
        );
    }

    @Override
    public Incident convert(IncidentAuthenticationLogoutMin incidentAuthenticationLogoutMin) {
        return this.convertUsername(
                AUTHENTICATION_LOGOUT_MIN.toString(),
                incidentAuthenticationLogoutMin.username()
        );
    }

    @Override
    public Incident convertUsername(String incidentType, Username username) {
        var incident = new Incident();
        incident.add(IncidentAttributes.Keys.TYPE, incidentType);
        incident.add(IncidentAttributes.Keys.USERNAME, username);
        return incident;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private Incident convertAuthenticationLoginFailure(String incidentType, Username username, Password password) {
        var incident = this.convertUsername(incidentType, username);
        incident.add(IncidentAttributes.Keys.PASSWORD, password);
        return incident;
    }
}
