package io.tech1.framework.domain.utilities.strings;

import lombok.experimental.UtilityClass;

import static java.util.Objects.isNull;

@UtilityClass
public class StringUtility {

    public static boolean isNullOrEmpty(String value) {
        return isNull(value) || value.isEmpty();
    }

    public static boolean isNullOrBlank(String value) {
        return isNull(value) || value.isBlank();
    }
}
