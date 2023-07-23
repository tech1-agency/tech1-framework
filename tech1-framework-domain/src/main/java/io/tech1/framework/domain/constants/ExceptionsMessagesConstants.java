package io.tech1.framework.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionsMessagesConstants {
    public static final String CONTACT_DEVELOPMENT_TEAM = "%s. Please contact development team";

    public static final String INVALID_ATTRIBUTE = "Attribute `%s` is invalid";

    public static final String ENTITY_NOT_FOUND_FULL = "%s is not found. Value: `%s`";
    public static final String ENTITY_NOT_FOUND_SHORT = "%s is not found";
    public static final String ENTITY_ALREADY_USED = "%s is already used";

    public static final String ACCESS_DENIED = "Access denied. Username: `%s`, Entity: `%s`. Value: `%s`";

    public static final String MISSING_MAPPINGS_KEYS = "Attribute `%s` requirements: `[%s]`, disjunction: `[%s]`";

    // =================================================================================================================
    // Tests
    // =================================================================================================================
    public static final String PARAMETRIZED_TEST_CASE = "Execute parametrized test case. Source: `%s`. Actual: `%s`. Expected: `%s`";
}
