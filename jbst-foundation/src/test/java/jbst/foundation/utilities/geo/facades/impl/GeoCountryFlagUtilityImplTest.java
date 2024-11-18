package jbst.foundation.utilities.geo.facades.impl;

import jbst.foundation.configurations.ConfigurationPropertiesJbstHardcoded;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.domain.tests.constants.TestsFlagsConstants;
import jbst.foundation.utilities.geo.facades.GeoCountryFlagUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static jbst.foundation.domain.constants.StringConstants.UNDEFINED;
import static jbst.foundation.domain.constants.StringConstants.UNKNOWN;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class GeoCountryFlagUtilityImplTest {

    private static Stream<Arguments> getFlagEmojiTest() {
        return Stream.of(
                Arguments.of(null, null, TestsFlagsConstants.UNKNOWN),
                Arguments.of("Ukraine", "UA", TestsFlagsConstants.UKRAINE),
                Arguments.of("Portugal", "PT", TestsFlagsConstants.PORTUGAL),
                Arguments.of("United States", "US", TestsFlagsConstants.USA),
                Arguments.of(UNKNOWN, UNKNOWN, TestsFlagsConstants.UNKNOWN),
                Arguments.of(UNDEFINED, UNDEFINED, TestsFlagsConstants.UNKNOWN)
        );
    }

    @Configuration
    @Import({
            ConfigurationPropertiesJbstHardcoded.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ResourceLoader resourceLoader;
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        GeoCountryFlagUtility geoCountryFlagUtility() {
            return new GeoCountryFlagUtilityImpl(
                    this.resourceLoader,
                    this.applicationFrameworkProperties
            );
        }
    }

    private final GeoCountryFlagUtility componentUnderTest;

    @ParameterizedTest
    @MethodSource("getFlagEmojiTest")
    void getFlagEmojiTest(String country, String countryCode, String expected) {
        // Act
        var actual1 = this.componentUnderTest.getFlagEmojiByCountry(country);
        var actual2 = this.componentUnderTest.getFlagEmojiByCountryCode(countryCode);

        // Assert
        assertThat(actual1).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected);
    }
}
