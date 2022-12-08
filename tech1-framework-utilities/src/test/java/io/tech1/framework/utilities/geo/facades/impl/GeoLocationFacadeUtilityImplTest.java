package io.tech1.framework.utilities.geo.facades.impl;

import io.tech1.framework.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import io.tech1.framework.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import io.tech1.framework.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
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

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeoLocationFacadeUtilityImplTest {

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
    public void beforeEach() {
        reset(
                this.ipapiGeoLocationUtility,
                this.mindMaxGeoLocationUtility
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.ipapiGeoLocationUtility,
                this.mindMaxGeoLocationUtility
        );
    }

    @Test
    public void getGeoLocationThrowExceptionTest() throws GeoLocationNotFoundException {
        // Arrange
        var ipAddress = randomIPAddress();
        var geoLocation = randomGeoLocation();
        when(this.ipapiGeoLocationUtility.getGeoLocation(ipAddress)).thenThrow(new GeoLocationNotFoundException(randomString()));
        when(this.mindMaxGeoLocationUtility.getGeoLocation(ipAddress)).thenReturn(geoLocation);

        // Act
        var actual = this.componentUnderTest.getGeoLocation(ipAddress);

        // Assert
        verify(this.ipapiGeoLocationUtility).getGeoLocation(eq(ipAddress));
        verify(this.mindMaxGeoLocationUtility).getGeoLocation(eq(ipAddress));
        assertThat(actual).isEqualTo(geoLocation);
    }

    @Test
    public void getGeoLocationTest() throws GeoLocationNotFoundException {
        // Arrange
        var ipAddress = randomIPAddress();
        var geoLocation = randomGeoLocation();
        when(this.ipapiGeoLocationUtility.getGeoLocation(ipAddress)).thenReturn(geoLocation);

        // Act
        var actual = this.componentUnderTest.getGeoLocation(ipAddress);

        // Assert
        verify(this.ipapiGeoLocationUtility).getGeoLocation(eq(ipAddress));
        assertThat(actual).isEqualTo(geoLocation);
    }
}
