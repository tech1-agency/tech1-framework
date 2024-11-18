package jbst.foundation.domain.constants;

import lombok.experimental.UtilityClass;

import static jbst.foundation.domain.constants.JbstConstants.Logs.FRAMEWORK_B2B_SECURITY_JWT_PREFIX;
import static jbst.foundation.domain.constants.JbstConstants.Logs.PREFIX;

@UtilityClass
public class FrameworkLogsConstants {
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
    // Server
    // =================================================================================================================
    public static final String SERVER_RESET_SERVER_TASK = PREFIX + " Reset Server Initiator: `{}`. Status: `{}`";
}
