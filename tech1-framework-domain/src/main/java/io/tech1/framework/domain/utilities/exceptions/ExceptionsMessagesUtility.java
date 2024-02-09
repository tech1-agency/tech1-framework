package io.tech1.framework.domain.utilities.exceptions;

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

    public static String invalidAttributeRequiredMissingValues(String attributeName, String required, String missing) {
        return String.format(INVALID_ATTRIBUTE_REQUIRED_MISSING_VALUES, attributeName, required, missing);
    }

    public static String entityNotFound(String entity, String entityId) {
        return String.format(ENTITY_NOT_FOUND, entity, entityId);
    }

    public static String entityAlreadyUsed(String entity, String entityId) {
        return String.format(ENTITY_ALREADY_USED, entity, entityId);
    }

    public static String entityAccessDenied(String entity, String entityId) {
        return String.format(ENTITY_ACCESS_DENIED, entity, entityId);
    }
}
