package io.tech1.framework.domain.utilities.system;

import io.tech1.framework.domain.constants.SystemPropertiesConstants;
import io.tech1.framework.domain.enums.PrinterLevel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemProperties {
    private static final PrinterLevel PRINTER = PrinterLevel.findOrDebug(System.getProperty(SystemPropertiesConstants.TECH1_FRAMEWORK_PRINTER));

    public static PrinterLevel getPrinterLevel() {
        return PRINTER;
    }

    public static boolean isPrinterEnabled() {
        return "true".equals(System.getProperty(SystemPropertiesConstants.TECH1_FRAMEWORK_PRINTER));
    }
}
