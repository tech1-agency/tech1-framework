package io.tech1.framework.utilities.spring.actuator.info;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.info.SpringBootActuatorInfo.undefinedSpringBootActuatorInfo;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoGit.undefinedSpringBootActuatorInfoGit;
import static org.assertj.core.api.Assertions.assertThat;

class SpringBootActuatorInfoTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> deserializeTest() {
        return Stream.of(
                Arguments.of(undefinedSpringBootActuatorInfo(), "[?]", true, "info-1.json"),
                Arguments.of(new SpringBootActuatorInfo(
                        undefinedSpringBootActuatorInfoGit(),
                        new ArrayList<>(),
                        null
                ), "[?]", true, "info-2.json"),
                Arguments.of(new SpringBootActuatorInfo(
                        undefinedSpringBootActuatorInfoGit(),
                        new ArrayList<>(List.of("dev", "prod")),
                        null
                ), "dev", false, "info-3.json"),
                Arguments.of(new SpringBootActuatorInfo(
                        undefinedSpringBootActuatorInfoGit(),
                        null,
                        "stage"
                ), "stage", false, "info-4.json"),
                Arguments.of(new SpringBootActuatorInfo(
                        undefinedSpringBootActuatorInfoGit(),
                        null,
                        null
                ), "[?]", true, "info-5.json")
        );
    }

    @Override
    protected String getFolder() {
        return "spring/actuator/info";
    }

    // serialization ignored deliberately

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("deserializeTest")
    void deserializeTest(SpringBootActuatorInfo springBootActuatorInfo, String profile, boolean isUndefined, String fileName) {
        // Arrange
        var json = readFile(this.getFolder(), fileName);
        var typeReference = new TypeReference<SpringBootActuatorInfo>() {};

        // Act
        var actual = OBJECT_MAPPER.readValue(json, typeReference);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(springBootActuatorInfo);
        assertThat(actual.getProfile()).isEqualTo(profile);
        assertThat(actual.isUndefined()).isEqualTo(isUndefined);
    }
}
