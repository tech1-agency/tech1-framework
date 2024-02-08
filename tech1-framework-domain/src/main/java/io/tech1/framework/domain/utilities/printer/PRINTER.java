package io.tech1.framework.domain.utilities.printer;

import io.tech1.framework.domain.utilities.system.SystemProperties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PRINTER {

    // =================================================================================================================
    // PRINT
    // =================================================================================================================
    private static void print(String message) {
        var printerLevel = SystemProperties.getPrinterLevel();
        if (printerLevel.isError()) {
            error(message);
        } else if (printerLevel.isWarn()) {
            warn(message);
        } else if (printerLevel.isInfo()) {
            info(message);
        } else {
            debug(message);
        }
    }

    private static void print(String format, Object arg) {
        var printerLevel = SystemProperties.getPrinterLevel();
        if (printerLevel.isError()) {
            error(format, arg);
        } else if (printerLevel.isWarn()) {
            warn(format, arg);
        } else if (printerLevel.isInfo()) {
            info(format, arg);
        } else {
            debug(format, arg);
        }
    }

    private static void print(String format, Object... arguments) {
        var printerLevel = SystemProperties.getPrinterLevel();
        if (printerLevel.isError()) {
            error(format, arguments);
        } else if (printerLevel.isWarn()) {
            warn(format, arguments);
        } else if (printerLevel.isInfo()) {
            info(format, arguments);
        } else {
            debug(format, arguments);
        }
    }

    // =================================================================================================================
    // DEBUG
    // =================================================================================================================

    private static void debug(String message) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.debug(message);
        }
    }

    private static void debug(String format, Object arg) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.debug(format, arg);
        }
    }

    private static void debug(String format, Object... arguments) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.debug(format, arguments);
        }
    }

    // =================================================================================================================
    // INFO
    // =================================================================================================================

    public static void info(String message) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.info(message);
        }
    }

    public static void info(String format, Object arg) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.info(format, arg);
        }
    }

    public static void info(String format, Object... arguments) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.info(format, arguments);
        }
    }

    // =================================================================================================================
    // INFO
    // =================================================================================================================

    private static void warn(String message) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.info(message);
        }
    }

    private static void warn(String format, Object arg) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.info(format, arg);
        }
    }

    private static void warn(String format, Object... arguments) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.info(format, arguments);
        }
    }

    // =================================================================================================================
    // ERROR
    // =================================================================================================================

    public static void error(String message) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.error(message);
        }
    }

    public static void error(String format, Object arg) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.error(format, arg);
        }
    }

    public static void error(String format, Object... arguments) {
        if (SystemProperties.isPrinterEnabled()) {
            LOGGER.error(format, arguments);
        }
    }
}
