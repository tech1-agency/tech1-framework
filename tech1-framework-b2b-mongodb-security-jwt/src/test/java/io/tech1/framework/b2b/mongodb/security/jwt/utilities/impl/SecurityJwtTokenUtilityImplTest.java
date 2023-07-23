package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.domain.enums.TestAuthority;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.domain.tests.constants.TestsConstants;
import io.tech1.framework.domain.utilities.random.EntityUtility;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomPassword;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;
import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SecurityJwtTokenUtilityImplTest {

    private static Stream<Arguments> createJwtTokenTest() {
        return Stream.of(
                Arguments.of(TimeAmount.of(1L, HOURS)),
                Arguments.of(TimeAmount.of(10L, HOURS)),
                Arguments.of(TimeAmount.of(100L, HOURS)),
                Arguments.of(TimeAmount.of(1L, MINUTES)),
                Arguments.of(TimeAmount.of(10L, MINUTES)),
                Arguments.of(TimeAmount.of(100L, MINUTES)),
                Arguments.of(TimeAmount.of(1L, SECONDS)),
                Arguments.of(TimeAmount.of(10L, SECONDS)),
                Arguments.of(TimeAmount.of(100L, SECONDS))
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
                Arguments.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjIzIiwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sImlhdCI6MTY0MTkyNDM4OSwiZXhwIjo3OTUzMjcxNTg5fQ.RUiEg-2yMDjl5b-TOMwpuXfOGq5zTQiosO9IoteMDAo1", false),
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
        SecurityJwtTokenUtility securityJwtTokenUtility() {
            return new SecurityJwtTokenUtilityImpl(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final SecurityJwtTokenUtility componentUnderTest;

    @Test
    void createJwtAccessTokenTest() {
        // Arrange
        var expectedUsername = Username.of("multiuser43");
        var authorities = Arrays.asList(
                new SimpleGrantedAuthority(TestAuthority.ADMIN.getValue()),
                new SimpleGrantedAuthority(TestAuthority.USER.getValue())
        );
        var user = new DbUser(expectedUsername, randomPassword(), randomZoneId().getId(), authorities);
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();

        // Act
        var jwtAccessToken = this.componentUnderTest.createJwtAccessToken(user);

        // Assert
        var validatedClaims = this.componentUnderTest.validate(jwtAccessToken);
        assertThat(validatedClaims.safeGetUsername()).isEqualTo(expectedUsername);
        assertThat(validatedClaims.getClaims().getIssuedAt()).isBeforeOrEqualTo(new Date());
        var zoneId = user.getZoneId();
        var timeAmount = accessToken.getExpiration();
        var expiration = convertLocalDateTime(
                LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit()),
                zoneId
        );
        assertThat(validatedClaims.getClaims().getExpiration()).isBeforeOrEqualTo(expiration);
    }

    @Test
    void createJwtRefreshTokenTest() {
        // Arrange
        var expectedUsername = Username.of("multiuser43");
        var authorities = Arrays.asList(
                new SimpleGrantedAuthority(TestAuthority.ADMIN.getValue()),
                new SimpleGrantedAuthority(TestAuthority.USER.getValue())
        );
        var user = new DbUser(expectedUsername, randomPassword(), randomZoneId().getId(), authorities);
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();

        // Act
        var jwtRefreshToken = this.componentUnderTest.createJwtRefreshToken(user);

        // Assert
        var validatedClaims = this.componentUnderTest.validate(jwtRefreshToken);
        assertThat(validatedClaims.safeGetUsername()).isEqualTo(expectedUsername);
        assertThat(validatedClaims.getClaims().getIssuedAt()).isBeforeOrEqualTo(new Date());
        var zoneId = user.getZoneId();
        var timeAmount = refreshToken.getExpiration();
        var expiration = convertLocalDateTime(
                LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit()),
                zoneId
        );
        assertThat(validatedClaims.getClaims().getExpiration()).isBeforeOrEqualTo(expiration);
    }

    @ParameterizedTest
    @MethodSource("createJwtTokenTest")
    void createJwtTokenTest(TimeAmount timeAmount) {
        // Arrange
        var user = EntityUtility.entity(DbUser.class);

        // Act
        var jwtToken = this.componentUnderTest.createJwtToken(user, timeAmount);

        // Assert
        var validatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));
        assertThat(validatedClaims.safeGetUsername()).isEqualTo(user.getUsername());
        assertThat(validatedClaims.getClaims().getIssuedAt()).isBeforeOrEqualTo(new Date());
        var zoneId = nonNull(user.getZoneId()) ? user.getZoneId() : ZoneId.systemDefault();
        var expiration = convertLocalDateTime(
                LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit()),
                zoneId
        );
        assertThat(validatedClaims.getClaims().getExpiration()).isBeforeOrEqualTo(expiration);
    }

    @ParameterizedTest
    @MethodSource("validateTest")
    void validateTest(String jwtToken, boolean expected) {
        // Act
        var accessValidatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));
        var refreshValidatedClaims = this.componentUnderTest.validate(new JwtRefreshToken(jwtToken));

        // Assert
        assertThat(accessValidatedClaims.isValid()).isEqualTo(expected);
        assertThat(accessValidatedClaims.getJwtToken()).isEqualTo(jwtToken);
        assertThat(accessValidatedClaims.isAccess()).isEqualTo(true);
        assertThat(accessValidatedClaims.isRefresh()).isEqualTo(false);

        assertThat(refreshValidatedClaims.isValid()).isEqualTo(expected);
        assertThat(refreshValidatedClaims.getJwtToken()).isEqualTo(jwtToken);
        assertThat(refreshValidatedClaims.isAccess()).isEqualTo(false);
        assertThat(refreshValidatedClaims.isRefresh()).isEqualTo(true);
    }

    @ParameterizedTest
    @MethodSource("isExpiredTest")
    void isExpiredTest(String jwtToken, boolean expected) {
        // Act
        var jwtTokenValidatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));
        var actualExpired = this.componentUnderTest.isExpired(jwtTokenValidatedClaims);

        // Assert
        assertThat(actualExpired).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getUsernameByClaimsTest")
    void getUsernameByClaimsTest(String jwtToken, Username expectedUsername) {
        // Act
        var jwtTokenValidatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));
        var actualUsername = jwtTokenValidatedClaims.safeGetUsername();

        // Assert
        assertThat(actualUsername).isEqualTo(expectedUsername);
    }

    @ParameterizedTest
    @MethodSource("versionsTests")
    void getClaimsTest(String jwtToken, String expectedIssuedAt, String expectedExpiration, List<SimpleGrantedAuthority> expectedAuthorities) throws ParseException {
        // Arrange
        var sdf = new SimpleDateFormat(TestsConstants.DEFAULT_DATE_FORMAT_PATTERN);
        sdf.setTimeZone(TestsConstants.EET_TIME_ZONE);

        // Act
        var validatedClaims = this.componentUnderTest.validate(new JwtAccessToken(jwtToken));

        // Assert
        var claims = validatedClaims.getClaims();
        assertThat(claims.getIssuedAt()).isEqualTo(sdf.parse(expectedIssuedAt));
        assertThat(claims.getExpiration()).isEqualTo(sdf.parse(expectedExpiration));
        var actualAuthorities = Arrays.stream(claims.get("authorities").toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace("{", "")
                        .replace("}", "")
                        .replace("authority=", "")
                        .split(","))
                .map(rawUserRole -> new SimpleGrantedAuthority(rawUserRole.trim()))
                .collect(Collectors.toList());
        assertThat(actualAuthorities).isEqualTo(expectedAuthorities);
    }

}
