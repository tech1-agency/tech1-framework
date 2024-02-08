package io.tech1.framework.domain.enums;

import java.util.Arrays;

public enum PrinterLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR;

    public static PrinterLevel findOrDebug(String value) {
        return Arrays.stream(PrinterLevel.values())
                .filter(level -> level.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(PrinterLevel.DEBUG);
    }

    public boolean isDebug() {
        return DEBUG.equals(this);
    }

    public boolean isInfo() {
        return INFO.equals(this);
    }

    public boolean isWarn() {
        return WARN.equals(this);
    }

    public boolean isError() {
        return ERROR.equals(this);
    }
}
