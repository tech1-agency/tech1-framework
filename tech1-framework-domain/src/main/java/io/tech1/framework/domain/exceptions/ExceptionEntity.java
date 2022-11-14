package io.tech1.framework.domain.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.lang.System.currentTimeMillis;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class ExceptionEntity {
    private static final String ATTRIBUTE_SHORT_MESSAGE = "shortMessage";
    private static final String ATTRIBUTE_FULL_MESSAGE = "fullMessage";

    private final ExceptionEntityType exceptionEntityType;
    private final Map<String, Object> attributes;
    private final long timestamp;

    public ExceptionEntity(
            ExceptionEntityType exceptionEntityType,
            Map<String, Object> attributes
    ) {
        assertNonNullOrThrow(exceptionEntityType, invalidAttribute("ResponseExceptionEntity.exceptionEntityType"));
        this.exceptionEntityType = exceptionEntityType;
        this.attributes = attributes;
        this.timestamp = currentTimeMillis();
    }

    public static ExceptionEntity of(
            ExceptionEntityType exceptionEntityType,
            String shortMessage,
            String fullMessage
    ) {
        return new ExceptionEntity(
                exceptionEntityType,
                Map.of(
                        ATTRIBUTE_SHORT_MESSAGE, shortMessage,
                        ATTRIBUTE_FULL_MESSAGE, fullMessage
                )
        );
    }

    public static ExceptionEntity of(
            Exception exception
    ) {
        var exceptionMessage = exception.getMessage();
        return ExceptionEntity.of(
                ExceptionEntityType.ERROR,
                exceptionMessage,
                exceptionMessage
        );
    }
}
