package jbst.foundation.domain.constants;

import jbst.foundation.domain.enums.Toggle;
import lombok.experimental.UtilityClass;

import static jbst.foundation.domain.constants.JbstConstants.Logs.*;

@UtilityClass
public class FrameworkLogsConstants {
    // =================================================================================================================
    // Incidents
    // =================================================================================================================
    public static final String INCIDENT_FEATURE_DISABLED = PREFIX_INCIDENTS + " `{}` feature is " + Toggle.DISABLED.getLowerCase();
    public static final String INCIDENT = PREFIX_INCIDENTS + " `{}`. incident type: `{}`";
    public static final String INCIDENT_AUTHENTICATION_LOGIN = PREFIX_INCIDENTS + " `{}` - /login. Username: `{}`";
    public static final String INCIDENT_AUTHENTICATION_LOGIN_FAILURE = PREFIX_INCIDENTS + " `{}` - /login failure. Username: `{}`";
    public static final String INCIDENT_AUTHENTICATION_LOGOUT = PREFIX_INCIDENTS + " `{}` - :/logout. Username: `{}`";
    public static final String INCIDENT_REGISTER1 = PREFIX_INCIDENTS + " `{}` - /register1. Username: `{}`";
    public static final String INCIDENT_REGISTER1_FAILURE = PREFIX_INCIDENTS + " `{}` - /register1 failure. Username: `{}`";
    public static final String INCIDENT_SESSION_REFRESHED = PREFIX_INCIDENTS + " `{}` - /refreshToken. Username: `{}`";
    public static final String INCIDENT_SESSION_EXPIRED = PREFIX_INCIDENTS + " `{}` - session expired. Username: `{}`";
    public static final String INCIDENT_SYSTEM_RESET_SERVER = PREFIX_INCIDENTS + " `{}` - system reset server. Username: `{}`. Status: `{}`";

    // =================================================================================================================
    // SecurityJWT
    // =================================================================================================================
    public static final String SECURITY_JWT_AUTHENTICATION_LOGIN = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}` - /login. Username: `{}`";
    public static final String SECURITY_JWT_AUTHENTICATION_LOGIN_FAILURE = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}` - login failure. Username: `{}`";
    public static final String SECURITY_JWT_AUTHENTICATION_LOGOUT = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}`- /logout. Username: `{}`";
    public static final String SECURITY_JWT_REGISTER1 = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}`- /register1. Username: `{}`";
    public static final String SECURITY_JWT_REGISTER1_FAILURE = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}`- /register1 failure. Username: `{}`";
    public static final String SECURITY_JWT_SESSION_REFRESHED = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}`- /refreshToken. Username: `{}`";
    public static final String SECURITY_JWT_SESSION_EXPIRED = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}`- session expired. Username: `{}`";
    public static final String SECURITY_JWT_SESSION_ADD_USER_REQUEST_METADATA = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}`- Session add user request metadata. Username: `{}`";
    public static final String SECURITY_JWT_SESSION_RENEW_USER_REQUEST_METADATA = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " `{}`- Session renew user request metadata. Username: `{}`. Session: `{}`";

    // =================================================================================================================
    // SessionRegistry
    // =================================================================================================================
    public static final String SESSION_REGISTRY_REGISTER_SESSION = PREFIX_SESSION_REGISTRY + " Username `{}` - register session";
    public static final String SESSION_REGISTRY_RENEW_SESSION = PREFIX_SESSION_REGISTRY + " Username `{}` - renew session";
    public static final String SESSION_REGISTRY_REMOVE_SESSION = PREFIX_SESSION_REGISTRY + " Username `{}` - remove session";
    public static final String SESSION_REGISTRY_EXPIRE_SESSION = PREFIX_SESSION_REGISTRY + " Username `{}` - expire session";

    // =================================================================================================================
    // Server
    // =================================================================================================================
    public static final String SERVER_RESET_SERVER_TASK = PREFIX_SERVER + " Reset Server Initiator: `{}`. Status: `{}`";
}
