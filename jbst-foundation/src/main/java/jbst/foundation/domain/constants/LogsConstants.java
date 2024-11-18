package jbst.foundation.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogsConstants {
    public static final String SERVER_OFFLINE = "[Server]: `{}` is probably offline. Exception: `{}`";
    public static final String SERVER_CONTAINER_1 = "[Server]: `{}` container configuration. Version: `{}`. Status: `{}`";
    public static final String SERVER_CONTAINER_2 = "[Server]: `{}` container configuration. Status: `{}`";
    public static final String SERVER_STARTUP_LISTENER_1 = "[Server]: `{}` startup listener configuration. Version: `{}`. Status: `{}`";
    public static final String SERVER_STARTUP_LISTENER_2 = "[Server]: `{}` startup listener configuration. Status: `{}`";
    public static final String SERVER_AUTHENTICATION_ALLOWED = "[Server]: `{}` authentication allowed. Username: `{}`";
}
