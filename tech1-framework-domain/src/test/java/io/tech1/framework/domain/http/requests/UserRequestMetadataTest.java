package io.tech1.framework.domain.http.requests;

import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static io.tech1.framework.domain.http.requests.UserRequestMetadata.processed;
import static io.tech1.framework.domain.http.requests.UserRequestMetadata.processing;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class UserRequestMetadataTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(processing(mock(HttpServletRequest.class)), "user-request-metadata-1.json"),
                Arguments.of(processed(
                        GeoLocation.processed("127.0.0.1", "Ukraine", "Lviv"),
                        UserAgentDetails.processed("Chrome", "MacOS", "Mobile")
                ), "user-request-metadata-2.json"),
                Arguments.of(processed(
                        GeoLocation.unknown("127.0.0.1", "exception details on geo location"),
                        UserAgentDetails.unknown("exception details on user agent")
                ), "user-request-metadata-3.json"),
                Arguments.of(processed(
                        GeoLocation.processed("127.0.0.1", "Ukraine", "Lviv"),
                        UserAgentDetails.unknown("exception details on user agent")
                ), "user-request-metadata-4.json"),
                Arguments.of(processed(
                        GeoLocation.unknown("127.0.0.1", "exception details on geo location"),
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
    public void serializeTest(UserRequestMetadata userRequestMetadata, String fileName) {
        // Act
        var json = this.writeValueAsString(userRequestMetadata);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }
}
