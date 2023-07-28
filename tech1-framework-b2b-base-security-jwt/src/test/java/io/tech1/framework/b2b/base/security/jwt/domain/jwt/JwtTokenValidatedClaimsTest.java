package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomDate;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class JwtTokenValidatedClaimsTest {

    @Test
    void safeGetUsernameExceptionTest() {
        // Arrange
        var validatedClaims = JwtTokenValidatedClaims.invalid(entity(JwtAccessToken.class));

        // Act
        var throwable = catchThrowable(validatedClaims::safeGetUsername);

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Attribute `JwtTokenValidatedClaims` is invalid");
    }

    @Test
    void safeGetUsernameTest() {
        // Arrange
        var username = randomUsername();
        var claims = Jwts.claims().setSubject(username.identifier());
        var validatedClaims = JwtTokenValidatedClaims.valid(entity(JwtAccessToken.class), claims);

        // Act
        var actual = validatedClaims.safeGetUsername();

        // Assert
        assertThat(actual).isEqualTo(username);
    }

    @Test
    void safeGetExpirationTimestampExceptionTest() {
        // Arrange
        var validatedClaims = JwtTokenValidatedClaims.invalid(entity(JwtAccessToken.class));

        // Act
        var throwable = catchThrowable(validatedClaims::safeGetExpirationTimestamp);

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Attribute `JwtTokenValidatedClaims` is invalid");
    }

    @Test
    void safeGetExpirationTimestampTest() {
        // Arrange
        var date = randomDate();
        var claims = Jwts.claims().setExpiration(date);
        var validatedClaims = JwtTokenValidatedClaims.valid(entity(JwtAccessToken.class), claims);

        // Act
        var actual = validatedClaims.safeGetExpirationTimestamp();

        // Assert
        assertThat(actual).isEqualTo(date.getTime());
    }
}
