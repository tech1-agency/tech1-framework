package tech1.framework.foundation.configurations;

import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import tech1.framework.foundation.domain.base.PropertyId;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.utilities.browsers.UserAgentDetailsUtility;
import tech1.framework.foundation.utilities.browsers.impl.UserAgentDetailsUtilityImpl;
import tech1.framework.foundation.utilities.geo.facades.GeoCountryFlagUtility;
import tech1.framework.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
import tech1.framework.foundation.utilities.geo.facades.impl.GeoCountryFlagUtilityImpl;
import tech1.framework.foundation.utilities.geo.facades.impl.GeoLocationFacadeUtilityImpl;
import tech1.framework.foundation.utilities.geo.functions.ipapi.feign.IPAPIFeign;
import tech1.framework.foundation.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import tech1.framework.foundation.utilities.geo.functions.ipapi.utility.impl.IPAPIGeoLocationUtilityImpl;
import tech1.framework.foundation.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
import tech1.framework.foundation.utilities.geo.functions.mindmax.impl.MindMaxGeoLocationUtilityImpl;
import tech1.framework.foundation.utils.UserMetadataUtils;
import tech1.framework.foundation.utils.impl.UserMetadataUtilsImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationUserMetadata {

    // Resources
    private final ResourceLoader resourceLoader;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getUtilitiesConfigs().assertProperties(new PropertyId("utilitiesConfigs"));
    }

    @Bean
    IPAPIFeign ipapiFeign() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .retryer(Retryer.NEVER_RETRY)
                .target(IPAPIFeign.class, "http://ip-api.com");
    }

    @Bean
    UserAgentDetailsUtility userAgentDetailsUtility() {
        return new UserAgentDetailsUtilityImpl(
                this.applicationFrameworkProperties
        );
    }

    @Bean
    GeoCountryFlagUtility geoCountryFlagUtility() {
        return new GeoCountryFlagUtilityImpl(
                this.resourceLoader,
                this.applicationFrameworkProperties
        );
    }

    @Bean
    IPAPIGeoLocationUtility ipapiGeoLocationUtility() {
        return new IPAPIGeoLocationUtilityImpl(
                this.ipapiFeign(),
                this.geoCountryFlagUtility()
        );
    }

    @Bean
    MindMaxGeoLocationUtility mindMaxGeoLocationUtility() {
        return new MindMaxGeoLocationUtilityImpl(
                this.resourceLoader,
                this.geoCountryFlagUtility(),
                this.applicationFrameworkProperties
        );
    }

    @Bean
    GeoLocationFacadeUtility geoLocationFacadeUtility() {
        return new GeoLocationFacadeUtilityImpl(
                this.ipapiGeoLocationUtility(),
                this.mindMaxGeoLocationUtility()
        );
    }

    @Bean
    UserMetadataUtils userMetadataUtils() {
        return new UserMetadataUtilsImpl(
                this.geoLocationFacadeUtility(),
                this.userAgentDetailsUtility()
        );
    }

}
