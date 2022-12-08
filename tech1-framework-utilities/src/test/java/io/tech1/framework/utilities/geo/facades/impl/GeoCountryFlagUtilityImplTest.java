package io.tech1.framework.utilities.geo.facades.impl;

import io.tech1.framework.utilities.geo.facades.GeoCountryFlagUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeoCountryFlagUtilityImplTest {

    private static Stream<Arguments> getEmojiTest() {
        return Stream.of(
                Arguments.of(null, null, "🏴‍"),
                Arguments.of("Ukraine", "UA", "🇺🇦"),
                Arguments.of("Portugal", "PT", "🇵🇹"),
                Arguments.of("United States", "US", "🇺🇸"),
                Arguments.of(UNKNOWN, UNKNOWN, "🏴‍"),
                Arguments.of(UNDEFINED, UNDEFINED, "🏴‍")
        );
    }

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ResourceLoader resourceLoader;

        @Bean
        GeoCountryFlagUtility geoCountryFlagUtility() {
            return new GeoCountryFlagUtilityImpl(
                    this.resourceLoader
            );
        }
    }

    private final GeoCountryFlagUtility componentUnderTest;

    @ParameterizedTest
    @MethodSource("getEmojiTest")
    public void getEmojiTest(String country, String countryCode, String expected) {
        // Act
        var actual1 = this.componentUnderTest.getEmojiByCountry(country);
        var actual2 = this.componentUnderTest.getEmojiByCountryCode(countryCode);

        // Assert
        assertThat(actual1).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected);
    }
}
