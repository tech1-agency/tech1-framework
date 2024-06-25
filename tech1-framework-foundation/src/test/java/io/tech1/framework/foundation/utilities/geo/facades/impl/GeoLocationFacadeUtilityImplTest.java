package io.tech1.framework.foundation.utilities.geo.facades.impl;

import io.tech1.framework.foundation.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.foundation.domain.geo.GeoLocation;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import io.tech1.framework.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import io.tech1.framework.foundation.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class GeoLocationFacadeUtilityImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IPAPIGeoLocationUtility ipapiGeoLocationUtility() {
            return mock(IPAPIGeoLocationUtility.class);
        }

        @Bean
        MindMaxGeoLocationUtility mindMaxGeoLocationUtility() {
            return mock(MindMaxGeoLocationUtility.class);
        }

        @Bean
        GeoLocationFacadeUtility geoLocationFacade() {
            return new GeoLocationFacadeUtilityImpl(
                    this.ipapiGeoLocationUtility(),
                    this.mindMaxGeoLocationUtility()
            );
        }
    }

    private final IPAPIGeoLocationUtility ipapiGeoLocationUtility;
    private final MindMaxGeoLocationUtility mindMaxGeoLocationUtility;

    private final GeoLocationFacadeUtility componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.ipapiGeoLocationUtility,
                this.mindMaxGeoLocationUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.ipapiGeoLocationUtility,
                this.mindMaxGeoLocationUtility
        );
    }

    @Test
    void getGeoLocationThrowExceptionTest() throws GeoLocationNotFoundException {
        // Arrange
        var ipAddress = IPAddress.random();
        var geoLocation = GeoLocation.random();
        when(this.ipapiGeoLocationUtility.getGeoLocation(ipAddress)).thenThrow(new GeoLocationNotFoundException(randomString()));
        when(this.mindMaxGeoLocationUtility.getGeoLocation(ipAddress)).thenReturn(geoLocation);

        // Act
        var actual = this.componentUnderTest.getGeoLocation(ipAddress);

        // Assert
        verify(this.ipapiGeoLocationUtility).getGeoLocation(ipAddress);
        verify(this.mindMaxGeoLocationUtility).getGeoLocation(ipAddress);
        assertThat(actual).isEqualTo(geoLocation);
    }

    @Test
    void getGeoLocationTest() throws GeoLocationNotFoundException {
        // Arrange
        var ipAddress = IPAddress.random();
        var geoLocation = GeoLocation.random();
        when(this.ipapiGeoLocationUtility.getGeoLocation(ipAddress)).thenReturn(geoLocation);

        // Act
        var actual = this.componentUnderTest.getGeoLocation(ipAddress);

        // Assert
        verify(this.ipapiGeoLocationUtility).getGeoLocation(ipAddress);
        assertThat(actual).isEqualTo(geoLocation);
    }
}
