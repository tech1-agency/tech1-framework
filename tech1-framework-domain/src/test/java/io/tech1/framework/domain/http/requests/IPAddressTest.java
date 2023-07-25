package io.tech1.framework.domain.http.requests;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomIPv4;
import static org.assertj.core.api.Assertions.assertThat;

class IPAddressTest {

    @Test
    void constructorsNullValueTest() {
        // Act
        var actual = new IPAddress(null);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isEqualTo("127.0.0.1");
    }

    @Test
    void constructorsValidValueTest() {
        // Arrange
        var value = randomIPv4();

        // Act
        var actual = new IPAddress(value);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isEqualTo(value);
    }
}
