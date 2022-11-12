package io.tech1.framework.domain.utilities.exceptions;

import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import static io.tech1.framework.domain.constants.ExceptionsMessagesConstants.*;

@UtilityClass
public class ExceptionsMessagesUtility {

    public static String contactDevelopmentTeam(String prefix) {
        return String.format(CONTACT_DEVELOPMENT_TEAM, prefix);
    }

    public static String invalidAttribute(String attributeName) {
        return String.format(INVALID_ATTRIBUTE, attributeName);
    }

    public static String entityNotFoundShort(String entity) {
        return String.format(ENTITY_NOT_FOUND_SHORT, entity);
    }

    public static String entityNotFound(String entity, String value) {
        return String.format(ENTITY_NOT_FOUND_FULL, entity, value);
    }

    public static String entityAlreadyUsed(String entity) {
        return String.format(ENTITY_ALREADY_USED, entity);
    }

    public static String accessDenied(Username username, String entity, String value) {
        return String.format(ACCESS_DENIED, username, entity, value);
    }

    // =================================================================================================================
    // Tests
    // =================================================================================================================
    public static String parametrizedTestCase(Object source, Object actual, Object expected) {
        return String.format(PARAMETRIZED_TEST_CASE, source, actual, expected);
    }
}
