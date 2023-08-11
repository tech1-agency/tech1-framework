package io.tech1.framework.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionsMessagesConstants {
    public static final String CONTACT_DEVELOPMENT_TEAM = "%s. Please contact development team";

    public static final String INVALID_ATTRIBUTE = "Attribute `%s` is invalid";

    public static final String ENTITY_NOT_FOUND = "%s: Not Found, id = %s";
    public static final String ENTITY_ALREADY_USED = "%s: Already Used, id = %s";
    public static final String ENTITY_ACCESS_DENIED = "%s: Access Denied, id = %s";

    public static final String MISSING_MAPPINGS_KEYS = "Attribute `%s` requirements: `[%s]`, disjunction: `[%s]`";
}
