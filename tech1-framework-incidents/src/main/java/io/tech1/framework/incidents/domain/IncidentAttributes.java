package io.tech1.framework.incidents.domain;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IncidentAttributes {

    @UtilityClass
    public static class Keys {
        public static final String TYPE = "incidentType";

        public static final String USERNAME = "Username";
        public static final String PASSWORD = "Password";

        public static final String TRIGGER = "_Trigger";

        public static final String EXCEPTION = "Exception";
        public static final String TRACE = "Trace";
        public static final String MESSAGE = "Message";
        public static final String METHOD = "Method";
        public static final String PARAMS = "Params";

        public static final String INVITATION_CODE = "Invitation Code";

        public static final String BROWSER = "Browser";
        public static final String COUNTRY = "Country";
        public static final String IP_ADDRESS = "IP address";
        public static final String WHAT = "What";
        public static final String WHEN = "When";
        public static final String WHERE = "Where";
    }

    @UtilityClass
    public static class Values {
        public static final String THROWABLE = "Throwable";
        public static final String AUTHENTICATION_LOGIN = "Authentication Login";
        public static final String AUTHENTICATION_LOGIN_FAILURE = "Authentication Login Failure";
        public static final String AUTHENTICATION_LOGOUT = "Authentication Logout";
        public static final String SESSION_REFRESHED = "Session Refreshed";
        public static final String SESSION_EXPIRED = "Session Expired";
        public static final String REGISTER1 = "Register1";
        public static final String REGISTER1_FAILURE = "Register1 Failure";
    }
}
