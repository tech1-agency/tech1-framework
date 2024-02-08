package io.tech1.framework.domain.utilities.printer;

import io.tech1.framework.domain.utilities.system.SystemProperties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PRINTER {

    // =================================================================================================================
    // DEBUG
    // =================================================================================================================

    public static void debug(String message) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.debug(message);
        }
    }

    public static void debug(String format, Object arg) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.debug(format, arg);
        }
    }

    public static void debug(String format, Object... arguments) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.debug(format, arguments);
        }
    }

    // =================================================================================================================
    // INFO
    // =================================================================================================================

    public static void info(String message) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.info(message);
        }
    }

    public static void info(String format, Object arg) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.info(format, arg);
        }
    }

    public static void info(String format, Object... arguments) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.info(format, arguments);
        }
    }

    // =================================================================================================================
    // WARN
    // =================================================================================================================

    public static void warn(String message) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.warn(message);
        }
    }

    public static void warn(String format, Object arg) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.warn(format, arg);
        }
    }

    public static void warn(String format, Object... arguments) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.warn(format, arguments);
        }
    }

    // =================================================================================================================
    // ERROR
    // =================================================================================================================

    public static void error(String message) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.error(message);
        }
    }

    public static void error(String format, Object arg) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.error(format, arg);
        }
    }

    public static void error(String format, Object... arguments) {
        if (SystemProperties.PRINTER_ENABLED) {
            LOGGER.error(format, arguments);
        }
    }
}
