package io.tech1.framework.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionsMessagesConstants {
    public static final String CONTACT_DEVELOPMENT_TEAM = "%s. Please contact development team";

    public static final String INVALID_ATTRIBUTE = "Attribute `%s` is invalid";
    // TODO [YYL] Missing values -> Missing value
    public static final String INVALID_ATTRIBUTE_REQUIRED_MISSING_VALUES = "Attribute `%s` is invalid. Required values: `[%s]`. Missing values: `[%s]`";

    public static final String ENTITY_NOT_FOUND = "%s: Not Found, id = %s";
    public static final String ENTITY_ALREADY_USED = "%s: Already Used, id = %s";
    public static final String ENTITY_ACCESS_DENIED = "%s: Access Denied, id = %s";
}
