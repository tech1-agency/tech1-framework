package jbst.foundation.utilities.zones;

import jbst.foundation.domain.constants.JbstConstants;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZoneId;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ZonesUtilityTest {

    private static Stream<Arguments> getBasicAuthenticationHeaderTest() {
        return Stream.of(
                Arguments.of(ZoneId.of("Europe/Kiev"), JbstConstants.ZoneIds.UKRAINE),
                Arguments.of(ZoneId.of("Europe/Kiev"), JbstConstants.ZoneIds.UKRAINE),
                Arguments.of(ZoneId.of("Poland"), JbstConstants.ZoneIds.POLAND)
        );
    }

    @ParameterizedTest
    @MethodSource("getBasicAuthenticationHeaderTest")
    void getBasicAuthenticationHeaderTest(ZoneId zoneId, ZoneId expected) {
        // Act
        var actual = ZonesUtility.reworkUkraineZoneId(zoneId);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
