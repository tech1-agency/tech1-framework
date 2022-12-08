package io.tech1.framework.utilities.geo.functions.ipapi.utility.impl;

import io.tech1.framework.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.utilities.geo.functions.ipapi.domain.IPAPIResponse;
import io.tech1.framework.utilities.geo.functions.ipapi.feign.IPAPIFeign;
import io.tech1.framework.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
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

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomFeignException;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomIPAddress;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IPAPIGeoLocationUtilityImplTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        IPAPIFeign ipapiFeign() {
            return mock(IPAPIFeign.class);
        }

        @Bean
        IPAPIGeoLocationUtility ipapiGeoLocationUtility() {
            return new IPAPIGeoLocationUtilityImpl(
                    this.ipapiFeign()
            );
        }
    }

    private final IPAPIFeign ipapiFeign;

    private final IPAPIGeoLocationUtility componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.ipapiFeign
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.ipapiFeign
        );
    }

    @Test
    public void getGeoLocationThrowFeignExceptionTest() {
        // Arrange
        var ipAddress = randomIPAddress();
        var feignException = randomFeignException();
        when(this.ipapiFeign.getIPAPIResponse(ipAddress.getValue())).thenThrow(feignException);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.getGeoLocation(ipAddress));

        // Assert
        verify(this.ipapiFeign).getIPAPIResponse(ipAddress.getValue());
        assertThat(throwable.getClass()).isEqualTo(GeoLocationNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Geo location not found: " + feignException.getMessage());
    }

    @Test
    public void getGeoLocationAPIFailureTest() {
        // Arrange
        var ipAddress = randomIPAddress();
        var ipapiResponse = new IPAPIResponse("fail", null, null, null, "reserved range");
        when(this.ipapiFeign.getIPAPIResponse(ipAddress.getValue())).thenReturn(ipapiResponse);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.getGeoLocation(ipAddress));

        // Assert
        verify(this.ipapiFeign).getIPAPIResponse(ipAddress.getValue());
        assertThat(throwable.getClass()).isEqualTo(GeoLocationNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Geo location not found: reserved range");
    }

    @Test
    public void getGeoLocationTest() throws GeoLocationNotFoundException {
        // Arrange
        var ipAddress = randomIPAddress();
        var ipapiResponse = new IPAPIResponse("success", "Ukraine", "UA", "Lviv", null);
        when(this.ipapiFeign.getIPAPIResponse(ipAddress.getValue())).thenReturn(ipapiResponse);

        // Act
        var actual = this.componentUnderTest.getGeoLocation(ipAddress);

        // Assert
        verify(this.ipapiFeign).getIPAPIResponse(ipAddress.getValue());
        assertThat(actual.getIpAddr()).isEqualTo(ipAddress.getValue());
        assertThat(actual.getCountry()).isEqualTo("Ukraine");
        assertThat(actual.getCountryCode()).isEqualTo("UA");
        assertThat(actual.getCity()).isEqualTo("Lviv");
    }
}
