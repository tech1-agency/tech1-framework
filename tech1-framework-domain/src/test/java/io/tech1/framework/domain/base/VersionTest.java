package io.tech1.framework.domain.base;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.SMALL_ITERATIONS_COUNT;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;

class VersionTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> versionsTests() {
        return Stream.of(
                Arguments.of(Version.of("1.1"), "version-1.json"),
                Arguments.of(Version.undefined(), "version-2.json"),
                Arguments.of(Version.unknown(), "version-3.json")
        );
    }

    @Override
    protected String getFolder() {
        return "base";
    }

    @ParameterizedTest
    @MethodSource("versionsTests")
    void serializeTest(Version version, String fileName) {
        // Act
        var json = this.writeValueAsString(version);

        // Assert
        assertThat(json).isEqualTo(readFile(this.getFolder(), fileName));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("versionsTests")
    void deserializeTest(Version version, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<Version>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isEqualTo(version);
        assertThat(actual.value()).isEqualTo(version.value());
        assertThat(actual.toString()).hasToString(version.value());
    }

    @RepeatedTest(SMALL_ITERATIONS_COUNT)
    void randomUsernameTest() {
        // Act
        var actual = Version.random();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isNotNull();
    }
}
