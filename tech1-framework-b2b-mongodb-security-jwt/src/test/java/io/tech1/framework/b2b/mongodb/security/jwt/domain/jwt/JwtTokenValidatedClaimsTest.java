package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class JwtTokenValidatedClaimsTest {

    @Test
    public void safeGetUsernameExceptionTest() {
        // Arrange
        var validatedClaims = JwtTokenValidatedClaims.invalid(new JwtAccessToken(randomString()));

        // Act
        var throwable = catchThrowable(validatedClaims::safeGetUsername);

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).startsWith("Attribute `JwtTokenValidatedClaims` is invalid");
    }

    @Test
    public void safeGetUsernameTest() {
        // Arrange
        var username = randomUsername();
        var claims = Jwts.claims().setSubject(username.getIdentifier());
        var validatedClaims = JwtTokenValidatedClaims.valid(new JwtAccessToken(randomString()), claims);

        // Act
        var actual = validatedClaims.safeGetUsername();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(username);
    }

    @Test
    public void safeGetExpirationTimestampExceptionTest() {
        // Arrange
        var validatedClaims = JwtTokenValidatedClaims.invalid(new JwtAccessToken(randomString()));

        // Act
        var throwable = catchThrowable(validatedClaims::safeGetExpirationTimestamp);

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).startsWith("Attribute `JwtTokenValidatedClaims` is invalid");
    }

    @Test
    public void safeGetExpirationTimestampTest() {
        // Arrange
        var date = randomDate();
        var claims = Jwts.claims().setExpiration(date);
        var validatedClaims = JwtTokenValidatedClaims.valid(new JwtAccessToken(randomString()), claims);

        // Act
        var actual = validatedClaims.safeGetExpirationTimestamp();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(date.getTime());
    }
}
