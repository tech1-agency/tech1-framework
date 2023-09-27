package io.tech1.framework.domain.geo;

import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.geo.GeoLocation.*;
import static io.tech1.framework.domain.http.requests.IPAddress.localhost;
import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

class GeoLocationTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(processed(localhost(), "Ukraine", "UA", "🇺🇦", "Lviv"), "geo-location-1.json"),
                Arguments.of(unknown(localhost(), "exception details"), "geo-location-2.json"),
                Arguments.of(processing(localhost()), "geo-location-3.json"),
                Arguments.of(processed(localhost(), null, null, null, "Lviv"), "geo-location-4.json"),
                Arguments.of(unknown(null, "exception details"), "geo-location-5.json")
        );
    }

    private static Stream<Arguments> getWhereTest() {
        return Stream.of(
                Arguments.of(processed(localhost(), "Ukraine", "UA", "🇺🇦", "Lviv"), "Ukraine, Lviv"),
                Arguments.of(processed(localhost(), "Ukraine", "UA", "🇺🇦", ""), "Ukraine"),
                Arguments.of(processed(localhost(), "Ukraine", "UA", "🇺🇦", " "), "Ukraine"),
                Arguments.of(processed(localhost(), "Ukraine", "UA", "🇺🇦", "    "), "Ukraine"),
                Arguments.of(processed(localhost(), "Ukraine", "UA", "🇺🇦", null), "Ukraine"),
                Arguments.of(processed(localhost(), null, "UA", "🇺🇦", "Lviv"), "Unknown"),
                Arguments.of(processed(localhost(), "", "UA", "🇺🇦", "Lviv"), "Unknown"),
                Arguments.of(processed(localhost(), "", "UA", "🇺🇦", "Lviv"), "Unknown"),
                Arguments.of(processed(localhost(), "  ", "UA", "🇺🇦", "Lviv"), "Unknown"),
                Arguments.of(processed(localhost(), "     ", "UA", "🇺🇦", "Lviv"), "Unknown")
        );
    }

    @Override
    protected String getFolder() {
        return "geo";
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    void serializeTest(GeoLocation geoLocation, String fileName) {
        // Act
        var json = this.writeValueAsString(geoLocation);

        // Assert
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @ParameterizedTest
    @MethodSource("getWhereTest")
    void getWhereTest(GeoLocation geoLocation, String expected) {
        // Act
        var actual = geoLocation.getWhere();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void validTest() {
        // Act
        var actual = GeoLocation.valid();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getIpAddr()).isNotNull();
        assertThat(actual.getCountry()).isEqualTo("Ukraine");
        assertThat(actual.getCountryCode()).isEqualTo("UA");
        assertThat(actual.getCountryFlag()).isEqualTo("🇺🇦");
        assertThat(actual.getCity()).isEqualTo("Lviv");
        assertThat(actual.getExceptionDetails()).isEmpty();
        assertThat(actual.getWhere()).isEqualTo("Ukraine, Lviv");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void invalidTest() {
        // Act
        var actual = GeoLocation.invalid();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getIpAddr()).isNotNull();
        assertThat(actual.getCountry()).isEqualTo(UNKNOWN);
        assertThat(actual.getCity()).isEqualTo(UNKNOWN);
        assertThat(actual.getExceptionDetails()).isEqualTo("Location is unknown");
        assertThat(actual.getWhere()).isEqualTo("Unknown, Unknown");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomTest() {
        // Act
        var actual = GeoLocation.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getIpAddr()).isNotNull();
        assertThat(actual.getCountry()).isNotNull();
        assertThat(actual.getExceptionDetails()).isNotNull();
        assertThat(actual.getWhere()).isNotNull();
    }
}
