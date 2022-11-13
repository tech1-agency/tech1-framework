package io.tech1.framework.domain.geo;

import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static io.tech1.framework.domain.geo.GeoLocation.*;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomIpAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GeoLocationTest extends AbstractFolderSerializationRunner {

    @Override
    protected String getFolder() {
        return "geo";
    }

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(processed("127.0.0.1", "Ukraine", "Lviv"), "geo-location-1.json"),
                Arguments.of(unknown("127.0.0.1", "exception details"), "geo-location-2.json"),
                Arguments.of(processing(mock(HttpServletRequest.class)), "geo-location-3.json"),
                Arguments.of(processed("127.0.0.1", null, "Lviv"), "geo-location-4.json")
        );
    }

    private static Stream<Arguments> getWhereTest() {
        return Stream.of(
                Arguments.of(processed(randomIpAddress(), "Ukraine", "Lviv"), "Ukraine, Lviv"),
                Arguments.of(processed(randomIpAddress(), "Ukraine", ""), "Ukraine"),
                Arguments.of(processed(randomIpAddress(), "Ukraine", " "), "Ukraine"),
                Arguments.of(processed(randomIpAddress(), "Ukraine", "    "), "Ukraine"),
                Arguments.of(processed(randomIpAddress(), "Ukraine", null), "Ukraine"),
                Arguments.of(processed(randomIpAddress(), null, "Lviv"), "Unknown"),
                Arguments.of(processed(randomIpAddress(), "", "Lviv"), "Unknown"),
                Arguments.of(processed(randomIpAddress(), "", "Lviv"), "Unknown"),
                Arguments.of(processed(randomIpAddress(), "  ", "Lviv"), "Unknown"),
                Arguments.of(processed(randomIpAddress(), "     ", "Lviv"), "Unknown")
        );
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    public void serializeTest(GeoLocation geoLocation, String fileName) {
        // Act
        var json = this.writeValueAsString(geoLocation);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @ParameterizedTest
    @MethodSource("getWhereTest")
    public void getWhereTest(GeoLocation geoLocation, String expected) {
        // Act
        var actual = geoLocation.getWhere();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
