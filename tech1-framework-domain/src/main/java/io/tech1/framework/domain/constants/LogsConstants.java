package io.tech1.framework.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogsConstants {
    public static final String SERVER_OFFLINE = "[Server]: `{}` is probably offline. Exception: `{}`";
    public static final String SERVER_CONTAINER = "[Server]: `{}` container configuration. Version: `{}`. Status: `{}`";
    public static final String SERVER_STARTUP_LISTENER = "[Server]: `{}` startup listener configuration. Version: `{}`. Status: `{}`";
    public static final String SERVER_AUTHENTICATION_ALLOWED = "[Server]: `{}` authentication allowed. Username: `{}`";
}
