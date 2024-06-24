package io.tech1.framework.domain.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class Notification {
    @JsonProperty("nt")
    private final NotificationType notificationType;
    @JsonProperty("m")
    private final String message;

    public Notification(
            NotificationType notificationType,
            String message
    ) {
        assertNonNullOrThrow(notificationType, invalidAttribute("Notification.notificationType"));
        assertNonNullOrThrow(message, invalidAttribute("Notification.message"));
        this.notificationType = notificationType;
        this.message = message;
    }

    public static Notification info(String message) {
        return new Notification(NotificationType.INFO, message);
    }

    public static Notification success(String message) {
        return new Notification(NotificationType.SUCCESS, message);
    }

    public static Notification warning(String message) {
        return new Notification(NotificationType.WARNING, message);
    }

    public static Notification error(String message) {
        return new Notification(NotificationType.ERROR, message);
    }
}
