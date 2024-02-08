package io.tech1.framework.domain.utilities.system;

import io.tech1.framework.domain.constants.SystemPropertiesConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemProperties {

    public static boolean isPropertiesDebugEnabled() {
        return "true".equals(System.getProperty(SystemPropertiesConstants.TECH1_FRAMEWORK_PROPERTIES_DEBUG));
    }
}
