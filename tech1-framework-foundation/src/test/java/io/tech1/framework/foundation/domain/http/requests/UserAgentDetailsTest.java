package io.tech1.framework.foundation.domain.http.requests;

import io.tech1.framework.foundation.domain.tests.runners.AbstractFolderSerializationRunner;
import io.tech1.framework.foundation.domain.constants.StringConstants;
import io.tech1.framework.foundation.domain.tests.constants.TestsJunitConstants;
import io.tech1.framework.foundation.domain.tests.io.TestsIOUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserAgentDetailsTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> serializeTest() {
        return Stream.of(
                Arguments.of(UserAgentDetails.processed("Chrome", "macOS", "Desktop"), "user-agent-details-1.json"),
                Arguments.of(UserAgentDetails.processing(), "user-agent-details-2.json"),
                Arguments.of(UserAgentDetails.unknown("exception details"), "user-agent-details-3.json"),
                Arguments.of(UserAgentDetails.processed(null, "macOS", "Desktop"), "user-agent-details-4.json"),
                Arguments.of(UserAgentDetails.processed("Chrome", null, "Desktop"), "user-agent-details-5.json"),
                Arguments.of(UserAgentDetails.processed("Chrome", "macOS", null), "user-agent-details-6.json")
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
        assertThat(json).isEqualTo(TestsIOUtils.readFile(this.getFolder(), fileName));
    }

    @RepeatedTest(TestsJunitConstants.SMALL_ITERATIONS_COUNT)
    void validTest() {
        // Act
        var actual = UserAgentDetails.valid();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getBrowser()).isEqualTo("Chrome");
        assertThat(actual.getPlatform()).isEqualTo("macOS");
        assertThat(actual.getDeviceType()).isEqualTo("Desktop");
        assertThat(actual.getExceptionDetails()).isEmpty();
        assertThat(actual.getWhat()).isEqualTo("Chrome, macOS on Desktop");
    }

    @RepeatedTest(TestsJunitConstants.SMALL_ITERATIONS_COUNT)
    void invalidTest() {
        // Act
        var actual = UserAgentDetails.invalid();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getBrowser()).isEqualTo(StringConstants.UNKNOWN);
        assertThat(actual.getPlatform()).isEqualTo(StringConstants.UNKNOWN);
        assertThat(actual.getDeviceType()).isEqualTo(StringConstants.UNKNOWN);
        assertThat(actual.getExceptionDetails()).isEqualTo("User agent details are unknown");
        assertThat(actual.getWhat()).isEqualTo("Unknown, Unknown on Unknown");
    }

    @RepeatedTest(TestsJunitConstants.SMALL_ITERATIONS_COUNT)
    void randomTest() {
        // Act
        var userAgentDetails = UserAgentDetails.random();

        // Assert
        assertThat(userAgentDetails).isNotNull();
        assertThat(userAgentDetails.getBrowser()).isNotNull();
        assertThat(userAgentDetails.getPlatform()).isNotNull();
        assertThat(userAgentDetails.getDeviceType()).isNotNull();
        assertThat(userAgentDetails.getExceptionDetails()).isNotNull();
        assertThat(userAgentDetails.getWhat()).isNotNull();
    }
}
