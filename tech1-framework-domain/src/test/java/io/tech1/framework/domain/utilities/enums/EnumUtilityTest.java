package io.tech1.framework.domain.utilities.enums;

import io.tech1.framework.domain.tests.enums.EnumNoValuesUnderTests;
import io.tech1.framework.domain.tests.enums.EnumOneValueUnderTests;
import io.tech1.framework.domain.tests.enums.EnumUnderTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.enums.EnumUtility.getEnumNames;
import static org.assertj.core.api.Assertions.assertThat;

public class EnumUtilityTest {

    private static Stream<Arguments> getEnumNamesTest() {
        return Stream.of(
                Arguments.of(EnumNoValuesUnderTests.class, Set.of()),
                Arguments.of(EnumOneValueUnderTests.class, Set.of("ONE_VALUE")),
                Arguments.of(EnumUnderTests.class, Set.of("EXAMPLE_1", "EXAMPLE_2", "EXAMPLE_3", "EXAMPLE_4"))
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ParameterizedTest
    @MethodSource("getEnumNamesTest")
    public void getEnumNamesTest(Class<? extends Enum> enumClass, Set<String> expected) {
        // Act
        var actual = getEnumNames(enumClass);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
