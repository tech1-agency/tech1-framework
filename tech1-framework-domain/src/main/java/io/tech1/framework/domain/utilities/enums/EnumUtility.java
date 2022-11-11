package io.tech1.framework.domain.utilities.enums;

import lombok.experimental.UtilityClass;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@UtilityClass
public class EnumUtility {

    public static <E extends Enum<E>> Set<String> getEnumNames(Class<E> enumClass) {
        assertNonNullOrThrow(enumClass, invalidAttribute("enumClass"));
        return EnumSet.allOf(enumClass).stream().map(Enum::name).collect(Collectors.toSet());
    }
}
