package io.tech1.framework.domain.utilities.system;

import io.tech1.framework.domain.constants.SystemPropertiesConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemProperties {
    public static final boolean PRINTER_ENABLED = "true".equals(System.getProperty(SystemPropertiesConstants.TECH1_FRAMEWORK_PRINTER_ENABLED));
}
