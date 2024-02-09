package io.tech1.framework.domain.utilities.enums;

import io.tech1.framework.domain.enums.EnumValue;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttributeRequiredMissingValues;

@UtilityClass
public class EnumCreatorUtility {
    public static <E extends Enum<E> & EnumValue<String>> E findEnumIgnoreCaseOrThrow(Class<E> enumClass, String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> {
                    var message = invalidAttributeRequiredMissingValues(
                            enumClass.getName(),
                            baseJoining(enumClass),
                            value
                    );
                    return new IllegalArgumentException(message);
                });
    }
}
