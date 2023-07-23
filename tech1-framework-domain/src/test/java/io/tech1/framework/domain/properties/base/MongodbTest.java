package io.tech1.framework.domain.properties.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class MongodbTest {

    private static Stream<Arguments> noAuthenticationTest() {
        return Stream.of(
                Arguments.of("127.0.0.1", 27017, "tech1_framework_server", null, null),
                Arguments.of("127.0.0.1", 27017, "tech1_framework_server", randomString(), null),
                Arguments.of("127.0.0.1", 27017, "tech1_framework_server", null, randomString())
        );
    }

    @ParameterizedTest
    @MethodSource("noAuthenticationTest")
    void noAuthenticationTest(String host, int port, String database, String username, String password) {
        // Arrange
        var mongodb = new Mongodb();
        mongodb.setHost(host);
        mongodb.setPort(port);
        mongodb.setDatabase(database);
        mongodb.setUsername(username);
        mongodb.setPassword(password);

        // Act
        var actual = mongodb.connectionString();

        // Assert
        assertThat(actual).isEqualTo("mongodb://127.0.0.1:27017/tech1_framework_server");
    }

    @Test
    void authenticationPresentTest() {
        // Arrange
        var mongodb = new Mongodb();
        mongodb.setHost("127.0.0.1");
        mongodb.setPort(27017);
        mongodb.setDatabase("tech1_framework_server");
        mongodb.setUsername("admin");
        mongodb.setPassword("Password123!");

        // Act
        var actual = mongodb.connectionString();

        // Assert
        assertThat(actual).isEqualTo("mongodb://admin:Password123!@127.0.0.1:27017/tech1_framework_server");
    }
}
