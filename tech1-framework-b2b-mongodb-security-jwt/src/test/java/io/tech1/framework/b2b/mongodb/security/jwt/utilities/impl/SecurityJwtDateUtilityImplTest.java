package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtDateUtility;
import io.tech1.framework.domain.properties.base.TimeAmount;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.time.DateUtility.getAbsDifferenceByTimeUnit;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtDateUtilityImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        SecurityJwtDateUtility securityJwtDateUtility() {
            return new SecurityJwtDateUtilityImpl();
        }
    }

    private final SecurityJwtDateUtility componentUnderTest;

    @Test
    public void getIssuedAtTest() {
        // Arrange
        var expected = new Date();

        // Act
        var actual = this.componentUnderTest.getIssuedAt();

        // Assert
        assertThat(actual).isAfterOrEqualTo(expected);
    }

    private static Stream<Arguments> getExpirationTest() {
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

    @ParameterizedTest
    @MethodSource("getExpirationTest")
    public void getExpirationTest(TimeAmount timeAmount) {
        // Arrange
        var expected = new Date();
        var chronoUnit = timeAmount.getUnit();
        var expectedDifference = timeAmount.getAmount();

        // Act
        var actual = this.componentUnderTest.getExpiration(timeAmount);

        // Assert
        assertThat(actual).isAfter(expected);
        var actualDifference = getAbsDifferenceByTimeUnit(actual, expected, TimeUnit.of(chronoUnit));
        assertThat(actualDifference).isLessThanOrEqualTo(expectedDifference);
    }
}
