package io.tech1.framework.domain.utilities.exceptions;

import com.diogonunes.jcolor.AnsiFormat;
import io.tech1.framework.domain.base.PropertyId;
import lombok.experimental.UtilityClass;

import static com.diogonunes.jcolor.Attribute.RED_TEXT;

@UtilityClass
public class ExceptionConsoleUtility {
    private static final AnsiFormat RED_TEXT = new AnsiFormat(RED_TEXT());

    public static String invalidProperty(PropertyId propertyId) {
        return "Property \"%s\" is invalid".formatted(
                RED_TEXT.format(propertyId.value())
        );
    }
}
