package tech1.framework.foundation.utils.impl;

import tech1.framework.foundation.domain.enums.Status;
import tech1.framework.foundation.domain.http.requests.IPAddress;
import tech1.framework.foundation.domain.http.requests.UserAgentHeader;
import tech1.framework.foundation.utilities.browsers.UserAgentDetailsUtility;
import tech1.framework.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
import tech1.framework.foundation.utils.UserMetadataUtils;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserMetadataUtilsImplTest {

    @Configuration
    static class ContextConfiguration {

        @Bean
        GeoLocationFacadeUtility geoLocationFacadeUtility() {
            return mock(GeoLocationFacadeUtility.class);
        }

        @Bean
        UserAgentDetailsUtility userAgentDetailsUtility() {
            return mock(UserAgentDetailsUtility.class);
        }

        @Bean
        UserMetadataUtils userMetadataUtils() {
            return new UserMetadataUtilsImpl(
                    this.geoLocationFacadeUtility(),
                    this.userAgentDetailsUtility()
            );
        }
    }

    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final UserAgentDetailsUtility userAgentDetailsUtility;

    private final UserMetadataUtils componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.geoLocationFacadeUtility,
                this.userAgentDetailsUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.geoLocationFacadeUtility,
                this.userAgentDetailsUtility
        );
    }

    @Test
    void getUserRequestMetadataProcessed() {
        // Act
        var metadata = this.componentUnderTest.getUserRequestMetadataProcessed(
                IPAddress.localhost(),
                UserAgentHeader.testsHardcoded()
        );

        // Assert
        assertThat(metadata.getStatus()).isEqualTo(Status.COMPLETED);
        verify(this.geoLocationFacadeUtility).getGeoLocation(IPAddress.localhost());
        verify(this.userAgentDetailsUtility).getUserAgentDetails(UserAgentHeader.testsHardcoded());
    }
}
