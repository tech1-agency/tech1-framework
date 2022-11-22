package io.tech1.framework.domain.properties.base;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MongodbTest {

    @Test
    public void noAuthentication() {
        // Arrange
        var mongodb = new Mongodb();
        mongodb.setHost("127.0.0.1");
        mongodb.setPort(27017);
        mongodb.setDatabase("tech1_platform_server");

        // Act
        var actual = mongodb.connectionString();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("mongodb://127.0.0.1:27017/tech1_platform_server");
    }

    @Test
    public void authenticationPresent() {
        // Arrange
        var mongodb = new Mongodb();
        mongodb.setHost("127.0.0.1");
        mongodb.setPort(27017);
        mongodb.setDatabase("tech1_platform_server");
        mongodb.setUsername("admin");
        mongodb.setPassword("Password123!");

        // Act
        var actual = mongodb.connectionString();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("mongodb://admin:Password123!@127.0.0.1:27017/tech1_platform_server");
    }
}
