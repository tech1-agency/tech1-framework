package io.tech1.framework.domain.utilities.exceptions;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionsMessagesUtility {

    public static String pleaseWait(String prefix) {
        return "%s. Please wait...".formatted(prefix);
    }

    public static String contactDevelopmentTeam(String prefix) {
        return "%s. Please contact development team".formatted(prefix);
    }

    public static String notImplementedYet() {
        return contactDevelopmentTeam("Not implemented yet");
    }

    public static String invalidAttribute(String attributeName) {
        return "Attribute `%s` is invalid".formatted(attributeName);
    }

    public static String invalidAttributeOptionUnexpectedValue(String attributeName, String options, String unexpected) {
        return "Attribute `%s` is invalid. Options: `[%s]`. Unexpected: `[%s]`".formatted(attributeName, options, unexpected);
    }

    public static String entityNotFound(String entity, String entityId) {
        return "%s is not found. Id: %s".formatted(entity, entityId);
    }

    public static String entityAlreadyUsed(String entity, String entityId) {
        return "%s is already used. Id: %s".formatted(entity, entityId);
    }

    public static String entityAccessDenied(String entity, String entityId) {
        return "Access denied on %s. Id: %s".formatted(entity.toLowerCase(), entityId);
    }
}
