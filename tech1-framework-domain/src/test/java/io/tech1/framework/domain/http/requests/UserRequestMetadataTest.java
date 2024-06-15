package io.tech1.framework.domain.http.requests;

import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.http.requests.IPAddress.localhost;
import static io.tech1.framework.domain.http.requests.UserRequestMetadata.processed;
import static io.tech1.framework.domain.http.requests.UserRequestMetadata.processing;
import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

class UserRequestMetadataTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(processing(localhost()), "user-request-metadata-1.json"),
                Arguments.of(processed(
                        GeoLocation.processed(localhost(), "Ukraine", "UA", "🇺🇦", "Lviv"),
                        UserAgentDetails.processed("Chrome", "MacOS", "Mobile")
                ), "user-request-metadata-2.json"),
                Arguments.of(processed(
                        GeoLocation.unknown(localhost(), "exception details on geo location"),
                        UserAgentDetails.unknown("exception details on user agent")
                ), "user-request-metadata-3.json"),
                Arguments.of(processed(
                        GeoLocation.processed(localhost(), "Ukraine", "UA", "🇺🇦", "Lviv"),
                        UserAgentDetails.unknown("exception details on user agent")
                ), "user-request-metadata-4.json"),
                Arguments.of(processed(
                        GeoLocation.unknown(localhost(), "exception details on geo location"),
                        UserAgentDetails.processed("Chrome", "MacOS", "Mobile")
                ), "user-request-metadata-5.json")
        );
    }

    @Override
    protected String getFolder() {
        return "http/requests";
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    void serializeTest(UserRequestMetadata userRequestMetadata, String fileName) {
        // Act
        var json = this.writeValueAsString(userRequestMetadata);

        // Assert
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void validTest() {
        // Act
        var actual = UserRequestMetadata.valid();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(actual.getGeoLocation().getIpAddr()).isNotNull();
        assertThat(actual.getGeoLocation().getCountry()).isEqualTo("Ukraine");
        assertThat(actual.getGeoLocation().getCity()).isEqualTo("Lviv");
        assertThat(actual.getGeoLocation().getExceptionDetails()).isEmpty();
        assertThat(actual.getGeoLocation().getWhere()).isEqualTo("🇺🇦 Ukraine, Lviv");
        assertThat(actual.getUserAgentDetails().getBrowser()).isEqualTo("Chrome");
        assertThat(actual.getUserAgentDetails().getPlatform()).isEqualTo("macOS");
        assertThat(actual.getUserAgentDetails().getDeviceType()).isEqualTo("Desktop");
        assertThat(actual.getUserAgentDetails().getExceptionDetails()).isEmpty();
        assertThat(actual.getUserAgentDetails().getWhat()).isEqualTo("Chrome, macOS on Desktop");
        assertThat(actual.getWhatTuple2().a()).isEqualTo("Chrome");
        assertThat(actual.getWhatTuple2().b()).isEqualTo("Chrome, macOS on Desktop");
        assertThat(actual.getWhereTuple3().a()).isNotNull();
        assertThat(actual.getWhereTuple3().a().split("\\.")).hasSize(4);
        assertThat(actual.getWhereTuple3().b()).isEqualTo("🇺🇦");
        assertThat(actual.getWhereTuple3().c()).isEqualTo("🇺🇦 Ukraine, Lviv");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void invalidTest() {
        // Act
        var actual = UserRequestMetadata.invalid();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(actual.getGeoLocation().getIpAddr()).isNotNull();
        assertThat(actual.getGeoLocation().getCountry()).isEqualTo("Unknown");
        assertThat(actual.getGeoLocation().getCity()).isEqualTo("Unknown");
        assertThat(actual.getGeoLocation().getExceptionDetails()).isEqualTo("Location is unknown");
        assertThat(actual.getGeoLocation().getWhere()).isEqualTo("🏴‍ Unknown, Unknown");
        assertThat(actual.getUserAgentDetails().getBrowser()).isEqualTo("Unknown");
        assertThat(actual.getUserAgentDetails().getPlatform()).isEqualTo("Unknown");
        assertThat(actual.getUserAgentDetails().getDeviceType()).isEqualTo("Unknown");
        assertThat(actual.getUserAgentDetails().getExceptionDetails()).isEqualTo("User agent details are unknown");
        assertThat(actual.getUserAgentDetails().getWhat()).isEqualTo("Unknown, Unknown on Unknown");
        assertThat(actual.getWhatTuple2().a()).isEqualTo("Unknown");
        assertThat(actual.getWhatTuple2().b()).isEqualTo("Unknown, Unknown on Unknown");
        assertThat(actual.getWhereTuple3().a()).isNotNull();
        assertThat(actual.getWhereTuple3().a().split("\\.")).hasSize(4);
        assertThat(actual.getWhereTuple3().b()).isEqualTo("🏴‍");
        assertThat(actual.getWhereTuple3().c()).isEqualTo("🏴‍ Unknown, Unknown");
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomTest() {
        // Act
        var actual = UserRequestMetadata.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isNotNull();
        assertThat(actual.getGeoLocation().getIpAddr()).isNotNull();
        assertThat(actual.getGeoLocation().getCountry()).isNotNull();
        assertThat(actual.getGeoLocation().getCity()).isNotNull();
        assertThat(actual.getGeoLocation().getExceptionDetails()).isNotNull();
        assertThat(actual.getGeoLocation().getWhere()).isNotNull();
        assertThat(actual.getUserAgentDetails().getBrowser()).isNotNull();
        assertThat(actual.getUserAgentDetails().getPlatform()).isNotNull();
        assertThat(actual.getUserAgentDetails().getDeviceType()).isNotNull();
        assertThat(actual.getUserAgentDetails().getExceptionDetails()).isNotNull();
        assertThat(actual.getUserAgentDetails().getWhat()).isNotNull();
        assertThat(actual.getWhatTuple2().a()).isNotNull();
        assertThat(actual.getWhatTuple2().b()).isNotNull();
        assertThat(actual.getWhereTuple3().a()).isNotNull();
        assertThat(actual.getWhereTuple3().b()).isNotNull();
        assertThat(actual.getWhereTuple3().c()).isNotNull();
    }
}
