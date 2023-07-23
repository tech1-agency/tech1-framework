package io.tech1.framework.domain.http.requests;

import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.http.requests.UserAgentDetails.*;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

class UserAgentDetailsTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(processed("Chrome", "macOS", "Desktop"), "user-agent-details-1.json"),
                Arguments.of(processing(), "user-agent-details-2.json"),
                Arguments.of(unknown("exception details"), "user-agent-details-3.json"),
                Arguments.of(processed(null, "macOS", "Desktop"), "user-agent-details-4.json"),
                Arguments.of(processed("Chrome", null, "Desktop"), "user-agent-details-5.json"),
                Arguments.of(processed("Chrome", "macOS", null), "user-agent-details-6.json")
        );
    }

    @Override
    protected String getFolder() {
        return "http/requests";
    }

    @ParameterizedTest
    @MethodSource("serializeTest")
    void serializeTest(UserAgentDetails userAgentDetails, String fileName) {
        // Act
        var json = this.writeValueAsString(userAgentDetails);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }
}
