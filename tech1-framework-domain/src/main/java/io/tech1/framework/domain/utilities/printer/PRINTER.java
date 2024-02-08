package io.tech1.framework.domain.utilities.printer;

import io.tech1.framework.domain.utilities.system.SystemProperties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PRINTER {

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
