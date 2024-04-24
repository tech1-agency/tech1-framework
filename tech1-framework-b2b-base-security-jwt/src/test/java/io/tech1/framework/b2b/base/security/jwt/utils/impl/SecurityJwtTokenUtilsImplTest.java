package io.tech1.framework.b2b.base.security.jwt.utils.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenCreationParams;
import io.tech1.framework.b2b.base.security.jwt.tests.domain.enums.TestAuthority;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.constants.ZoneIdsConstants.UKRAINE;
import static io.tech1.framework.domain.tests.constants.TestsDTFsConstants.DEFAULT_DATE_FORMAT_PATTERN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;
import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Objects.nonNull;
import static java.util.TimeZone.getTimeZone;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SecurityJwtTokenUtilsImplTest {

    private static Stream<Arguments> createJwtTokenTest() {
        return Stream.of(
                Arguments.of(new TimeAmount(1L, HOURS)),
                Arguments.of(new TimeAmount(10L, HOURS)),
                Arguments.of(new TimeAmount(100L, HOURS)),
                Arguments.of(new TimeAmount(1L, MINUTES)),
                Arguments.of(new TimeAmount(10L, MINUTES)),
                Arguments.of(new TimeAmount(100L, MINUTES)),
                Arguments.of(new TimeAmount(1L, SECONDS)),
                Arguments.of(new TimeAmount(10L, SECONDS)),
                Arguments.of(new TimeAmount(100L, SECONDS))
        );
    }

    private static Stream<Arguments> validateTest() {
        return Stream.of(
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjIzIiwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sImlhdCI6MTY0MTkyNDM4OSwiZXhwIjo3OTUzMjcxNTg5fQ.RUiEg-2yMDjl5b-TOMwpuXfOGq5zTQiosO9IoteMDAo", true),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIiLCJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwiaWF0IjoxNjQxOTI0NDA5LCJleHAiOjc5NTMyNzE2MDl9.p9gNAWxL-vi81zy7ECfqqDnjQ7GfvhyDMJyKJ4iBQys", true),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiJdLCJpYXQiOjE2NDE5MzE4MjYsImV4cCI6MTY0MTkzMTgzNn0.EhKtfHCgOGrJEGR0G8czSHf1T6MywDYIKYpxlxV8GxI", true), // expired jwt token
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIiLCJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwiaWF0IjoxNjQxOTI0NDA5LCJleHAiOjc5NTMyNzE2MDl9.p9gNAWxL-vi81zy7ECfqqDnjQ7GfvhyDMJyKJ4iBQyq1", false),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIiLCJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwiaWF0IjoxNjQxOTI0NDA5LCJleHAiOjc5NTMyNzE2MDl9.p9gNAWxL-vi81zy7ECfqqDnjQ7GfvhyDMJyKJ4iBQyq2", false)
        );
    }

    private static Stream<Arguments> isExpiredTest() {
        return Stream.of(
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjIzIiwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sImlhdCI6MTY0MTkyNDM4OSwiZXhwIjo3OTUzMjcxNTg5fQ.RUiEg-2yMDjl5b-TOMwpuXfOGq5zTQiosO9IoteMDAo1", true),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjIzIiwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sImlhdCI6MTY0MTkyNDM4OSwiZXhwIjo3OTUzMjcxNTg5fQ.RUiEg-2yMDjl5b-TOMwpuXfOGq5zTQiosO9IoteMDAo", false),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIiLCJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwiaWF0IjoxNjQxOTI0NDA5LCJleHAiOjc5NTMyNzE2MDl9.p9gNAWxL-vi81zy7ECfqqDnjQ7GfvhyDMJyKJ4iBQys", false),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiJdLCJpYXQiOjE2NDE5MzE4MjYsImV4cCI6MTY0MTkzMTgzNn0.EhKtfHCgOGrJEGR0G8czSHf1T6MywDYIKYpxlxV8GxI", true)
        );
    }

    private static Stream<Arguments> getUsernameByClaimsTest() {
        return Stream.of(
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjIzIiwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sImlhdCI6MTY0MTkyNDM4OSwiZXhwIjo3OTUzMjcxNTg5fQ.RUiEg-2yMDjl5b-TOMwpuXfOGq5zTQiosO9IoteMDAo", Username.of("admin23")),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIiLCJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwiaWF0IjoxNjQxOTI0NDA5LCJleHAiOjc5NTMyNzE2MDl9.p9gNAWxL-vi81zy7ECfqqDnjQ7GfvhyDMJyKJ4iBQys", Username.of("user12")),
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiJdLCJpYXQiOjE2NDE5MzE4MjYsImV4cCI6MTY0MTkzMTgzNn0.EhKtfHCgOGrJEGR0G8czSHf1T6MywDYIKYpxlxV8GxI", Username.of("multiuser43")) // expired jwt token
        );
    }

    private static Stream<Arguments> versionsTests() {
        return Stream.of(
                Arguments.of(
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJ1c2VyIn1dLCJpYXQiOjE2NDIyMDUyNzQsImV4cCI6NDc5Nzg3ODg3NH0.eJejXwTEj9Noemfdggw5hsbA6W0ID71zkb5wELB6LIU",
                        "15.01.2022 02:07:54",
                        "15.01.2122 02:07:54",
                        List.of(
                                new SimpleGrantedAuthority(TestAuthority.USER.getValue())
                        )
                ),
                Arguments.of(
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhZG1pbiJ9LHsiYXV0aG9yaXR5IjoidXNlciJ9XSwiaWF0IjoxNjQyMjA1MTg4LCJleHAiOjQ3OTc4Nzg3ODh9.v8hqZe28EeZcODD84ycfl6JIUrPD6bgxFpxgn7PfLXk",
                        "15.01.2022 02:06:28",
                        "15.01.2122 02:06:28",
                        List.of(
                                new SimpleGrantedAuthority(TestAuthority.ADMIN.getValue()),
                                new SimpleGrantedAuthority(TestAuthority.USER.getValue())
                        )
                )
        );
    }

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        // Properties
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        SecurityJwtTokenUtils securityJwtTokenUtility() {
            return new SecurityJwtTokenUtilsImpl(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final SecurityJwtTokenUtils componentUnderTest;

    @Test
    void createJwtAccessTokenTest() {
        // Arrange
        var username = Username.of("multiuser43");
        var authorities = getSimpleGrantedAuthorities("admin", "user");
        var creationParams = new JwtTokenCreationParams(username, authorities, randomZoneId());

        // Act
        var accessToken = this.componentUnderTest.createJwtAccessToken(creationParams);

        // Assert
        var validatedClaims = this.componentUnderTest.validate(accessToken);
        assertThat(validatedClaims.username()).isEqualTo(username);
        var zoneId = creationParams.zoneId();
        var timeAmount = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken().getExpiration();
        var expiration = convertLocalDateTime(
                LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit()),
                zoneId
        );
        assertThat(validatedClaims.expirationDate()).isBeforeOrEqualTo(expiration);
    }

    @Test
    void createJwtRefreshTokenTest() {
        // Arrange
        var expectedUsername = Username.of("multiuser43");
        var authorities = getSimpleGrantedAuthorities("admin", "user");
        var creationParams = new JwtTokenCreationParams(expectedUsername, authorities, randomZoneId());

        // Act
        var refreshToken = this.componentUnderTest.createJwtRefreshToken(creationParams);

        // Assert
        var validatedClaims = this.componentUnderTest.validate(refreshToken);
        assertThat(validatedClaims.username()).isEqualTo(expectedUsername);
        var zoneId = creationParams.zoneId();
        var timeAmount = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken().getExpiration();
        var expiration = convertLocalDateTime(
                LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit()),
                zoneId
        );
        assertThat(validatedClaims.expirationDate()).isBeforeOrEqualTo(expiration);
    }

    @ParameterizedTest
    @MethodSource("createJwtTokenTest")
    void createJwtTokenTest(TimeAmount timeAmount) {
        // Arrange
        var creationParams = new JwtTokenCreationParams(Username.testsHardcoded(), getSimpleGrantedAuthorities("user"), randomZoneId());

        // Act
        var jwtToken = this.componentUnderTest.createJwtToken(creationParams, timeAmount);

        // Assert
        var validatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));
        assertThat(validatedClaims.username()).isEqualTo(Username.testsHardcoded());
        var zoneId = nonNull(creationParams.zoneId()) ? creationParams.zoneId() : ZoneId.systemDefault();
        var expiration = convertLocalDateTime(
                LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit()),
                zoneId
        );
        assertThat(validatedClaims.expirationDate()).isBeforeOrEqualTo(expiration);
    }

    @Disabled("Fix together with YYL")
    @ParameterizedTest
    @MethodSource("validateTest")
    void validateTest(String jwtToken, boolean expected) {
        // Act
        var accessValidatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));
        var refreshValidatedClaims = this.componentUnderTest.validate(new JwtRefreshToken(jwtToken));

        // Assert
        assertThat(accessValidatedClaims.valid()).isEqualTo(expected);
        assertThat(accessValidatedClaims.jwtToken()).isEqualTo(jwtToken);
        assertThat(accessValidatedClaims.isAccess()).isTrue();
        assertThat(accessValidatedClaims.isRefresh()).isFalse();
        assertThat(accessValidatedClaims.authorities()).isEmpty();

        assertThat(refreshValidatedClaims.valid()).isEqualTo(expected);
        assertThat(refreshValidatedClaims.jwtToken()).isEqualTo(jwtToken);
        assertThat(refreshValidatedClaims.isAccess()).isFalse();
        assertThat(refreshValidatedClaims.isRefresh()).isTrue();
        assertThat(refreshValidatedClaims.authorities()).isEmpty();
    }

    @Disabled("Fix together with YYL")
    @ParameterizedTest
    @MethodSource("isExpiredTest")
    void isExpiredTest(String jwtToken, boolean expected) {
        // Act
        var validatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));

        // Assert
        assertThat(validatedClaims.isExpired()).isEqualTo(expected);
    }

    @Disabled("Fix together with YYL")
    @ParameterizedTest
    @MethodSource("getUsernameByClaimsTest")
    void getUsernameByClaimsTest(String jwtToken, Username expectedUsername) {
        // Act
        var validatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));

        // Assert
        assertThat(validatedClaims.username()).isEqualTo(expectedUsername);
        assertThat(validatedClaims.authorities()).isEmpty();
    }

    @Disabled("Fix together with YYL")
    @ParameterizedTest
    @MethodSource("versionsTests")
    void getClaimsTest(String jwtToken, String expectedIssuedAt, String expectedExpiration, List<SimpleGrantedAuthority> authorities) throws ParseException {
        // Arrange
        var sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
        sdf.setTimeZone(getTimeZone(UKRAINE));

        // Act
        var validatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));

        // Assert
        assertThat(validatedClaims.issuedAt()).isEqualTo(sdf.parse(expectedIssuedAt));
        assertThat(validatedClaims.expirationDate()).isEqualTo(sdf.parse(expectedExpiration));
        assertThat(validatedClaims.authorities()).isEqualTo(authorities);
    }

}
