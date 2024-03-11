package io.tech1.framework.domain.asserts;

import com.diogonunes.jcolor.AnsiFormat;
import io.tech1.framework.domain.base.PropertyId;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.HashSet;

import static com.diogonunes.jcolor.Attribute.RED_TEXT;
import static io.tech1.framework.domain.utilities.collections.CollectionUtility.baseJoiningRaw;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionConsoleUtility.invalidProperty;
import static org.apache.commons.collections4.SetUtils.disjunction;

@UtilityClass
public class ConsoleAsserts {
    private static final AnsiFormat RED_TEXT = new AnsiFormat(RED_TEXT());

    public static <T> void assertContainsAllOrThrow(Collection<T> options, Collection<T> required, PropertyId propertyId) {
        if (!options.containsAll(required)) {
            throw new IllegalArgumentException(
                    "%s. Options: \"[%s]\". Required: \"[%s]\". Disjunction: \"[%s]\"".formatted(
                            invalidProperty(propertyId),
                            baseJoiningRaw(options),
                            baseJoiningRaw(required),
                            RED_TEXT.format(baseJoiningRaw(disjunction(new HashSet<>(options), new HashSet<>(required))))
                    )
            );
        }
    }

    public static <T> void assertEqualsOrThrow(Collection<T> options, Collection<T> required, PropertyId propertyId) {
        if (!options.equals(required)) {
            throw new IllegalArgumentException(
                    "%s. Options: \"[%s]\". Required: \"[%s]\". Disjunction: \"[%s]\"".formatted(
                            invalidProperty(propertyId),
                            baseJoiningRaw(options),
                            baseJoiningRaw(required),
                            RED_TEXT.format(baseJoiningRaw(disjunction(new HashSet<>(options), new HashSet<>(required))))
                    )
            );
        }
    }
}
