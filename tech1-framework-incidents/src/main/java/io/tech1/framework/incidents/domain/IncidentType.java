package io.tech1.framework.incidents.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IncidentType {
    AUTHENTICATION_LOGIN("Authentication Login"),
    AUTHENTICATION_LOGIN_FAILURE("Authentication Login Failure"),
    AUTHENTICATION_LOGOUT("Authentication Logout"),
    AUTHENTICATION_LOGOUT_MIN("Authentication Logout Min"),
    SESSION_REFRESHED("Session Refreshed"),
    SESSION_EXPIRED("Session Expired"),
    REGISTER1("Register1"),
    REGISTER1_FAILURE("Register1 Failure");

    private final String value;

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
