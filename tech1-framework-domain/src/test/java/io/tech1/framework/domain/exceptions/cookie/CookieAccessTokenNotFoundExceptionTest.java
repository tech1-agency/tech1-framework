package io.tech1.framework.domain.exceptions.cookie;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieAccessTokenNotFoundExceptionTest {

    @Test
    public void testException() {
        // Act
        var actual = new CookieAccessTokenNotFoundException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token not found");
    }
}
