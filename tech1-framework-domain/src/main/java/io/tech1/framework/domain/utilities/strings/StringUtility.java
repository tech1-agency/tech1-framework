package io.tech1.framework.domain.utilities.strings;

import lombok.experimental.UtilityClass;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@UtilityClass
public class StringUtility {

    public static boolean isNullOrEmpty(String value) {
        return isNull(value) || value.isEmpty();
    }

    public static boolean isNullOrBlank(String value) {
        return isNull(value) || value.isBlank();
    }

    public static boolean hasLength(String value) {
        return nonNull(value) && !value.isEmpty();
    }

    public static String getShortenValueOrUndefined(String value, int maxLength) {
        if (isNull(value)) {
            return UNDEFINED;
        }
        if (value.length() <= 3) {
            return value;
        }
        if (maxLength <= 3) {
            maxLength = 3;
        }
        return (value.length() > maxLength) ? value.substring(0, maxLength - 3) + "..." : value;
    }
}
