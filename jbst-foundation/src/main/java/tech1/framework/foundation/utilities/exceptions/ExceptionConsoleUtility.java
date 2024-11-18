package tech1.framework.foundation.utilities.exceptions;

import tech1.framework.foundation.domain.asserts.ConsoleAsserts;
import tech1.framework.foundation.domain.base.PropertyId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConsoleUtility {

    public static String invalidProperty(PropertyId propertyId) {
        return "Property \"%s\" is invalid".formatted(
                ConsoleAsserts.RED_TEXT.format(propertyId.value())
        );
    }
}
