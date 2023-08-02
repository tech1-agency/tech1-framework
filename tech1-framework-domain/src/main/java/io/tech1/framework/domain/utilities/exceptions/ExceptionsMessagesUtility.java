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

    public static String entityNotFoundId(String entity, String entityId) {
        return String.format(ENTITY_NOT_FOUND, entity, entityId);
    }

    @Deprecated
    public static String entityNotFoundShort(String entity) {
        return String.format(ENTITY_NOT_FOUND_SHORT, entity);
    }

    public static String entityAlreadyUsed(String entity) {
        return String.format(ENTITY_ALREADY_USED, entity);
    }

    // TODO [YY]
    @Deprecated
    public static String accessDeniedV1(Username username, String entity, String value) {
        return String.format(ACCESS_DENIED_V1, username, entity, value);
    }

    public static String accessDenied(String entity, String entityId) {
        return String.format(ENTITY_ACCESS_DENIED, entity, entityId);
    }

    public static String missingMappingsKeys(String attributeName, String requirements, String disjunction) {
        return String.format(MISSING_MAPPINGS_KEYS, attributeName, requirements, disjunction);
    }

    // =================================================================================================================
    // Tests
    // =================================================================================================================
    @Deprecated
    public static String parametrizedTestCase(Object source, Object actual, Object expected) {
        return String.format(PARAMETRIZED_TEST_CASE, source, actual, expected);
    }
}
