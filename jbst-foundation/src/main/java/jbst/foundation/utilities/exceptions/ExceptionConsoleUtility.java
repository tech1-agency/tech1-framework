package jbst.foundation.utilities.exceptions;

import jbst.foundation.domain.asserts.ConsoleAsserts;
import jbst.foundation.domain.base.PropertyId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConsoleUtility {

    public static String invalidProperty(PropertyId propertyId) {
        return "Property \"%s\" is invalid".formatted(
                ConsoleAsserts.RED_TEXT.format(propertyId.value())
        );
    }
}
