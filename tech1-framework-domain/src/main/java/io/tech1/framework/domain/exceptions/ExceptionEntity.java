package io.tech1.framework.domain.exceptions;

import io.tech1.framework.domain.utilities.strings.StringUtility;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;

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

    public ExceptionEntity(@NotNull ExceptionEntityType exceptionEntityType, Map<String, Object> attributes) {
        this.exceptionEntityType = exceptionEntityType;
        this.attributes = new HashMap<>(attributes);
        this.timestamp = getCurrentTimestamp();
    }

    public ExceptionEntity(ExceptionEntityType exceptionEntityType, String shortMessage, String fullMessage) {
        this(
                exceptionEntityType,
                Map.of(
                        ATTRIBUTE_SHORT_MESSAGE, shortMessage,
                        ATTRIBUTE_FULL_MESSAGE, fullMessage
                )
        );
    }

    public ExceptionEntity(MethodArgumentNotValidException exception) {
        this.exceptionEntityType = ExceptionEntityType.ERROR;
        var message = exception.getBindingResult().getFieldErrors().stream()
                .map(item -> StringUtility.convertCamelCaseToSplit(item.getField()) + " " + item.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(". "));
        this.attributes = Map.of(
                ATTRIBUTE_SHORT_MESSAGE, message,
                ATTRIBUTE_FULL_MESSAGE, message
        );
        this.timestamp = getCurrentTimestamp();
    }

    public ExceptionEntity(Exception exception) {
        this(
                ExceptionEntityType.ERROR,
                exception.getMessage(),
                exception.getMessage()
        );
    }

    public void addAttribute(String attributeKey, Object value) {
        this.attributes.put(attributeKey, value);
    }
}
