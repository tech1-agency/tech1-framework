package io.tech1.framework.foundation.configurations;

import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.foundation.utilities.browsers.impl.UserAgentDetailsUtilityImpl;
import io.tech1.framework.foundation.utilities.geo.facades.GeoCountryFlagUtility;
import io.tech1.framework.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
import io.tech1.framework.foundation.utilities.geo.facades.impl.GeoCountryFlagUtilityImpl;
import io.tech1.framework.foundation.utilities.geo.facades.impl.GeoLocationFacadeUtilityImpl;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.feign.IPAPIFeign;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.utility.impl.IPAPIGeoLocationUtilityImpl;
import io.tech1.framework.foundation.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
import io.tech1.framework.foundation.utilities.geo.functions.mindmax.impl.MindMaxGeoLocationUtilityImpl;
import io.tech1.framework.foundation.utils.UserMetadataUtils;
import io.tech1.framework.foundation.utils.impl.UserMetadataUtilsImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationUserMetadata {

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
    GeoCountryFlagUtility geoCountryFlagUtility(ResourceLoader resourceLoader) {
        return new GeoCountryFlagUtilityImpl(
                resourceLoader,
                this.applicationFrameworkProperties
        );
    }

    @Bean
    IPAPIGeoLocationUtility ipapiGeoLocationUtility(ResourceLoader resourceLoader) {
        return new IPAPIGeoLocationUtilityImpl(
                this.ipapiFeign(),
                this.geoCountryFlagUtility(resourceLoader)
        );
    }

    @Bean
    MindMaxGeoLocationUtility mindMaxGeoLocationUtility(ResourceLoader resourceLoader) {
        return new MindMaxGeoLocationUtilityImpl(
                resourceLoader,
                this.geoCountryFlagUtility(resourceLoader),
                this.applicationFrameworkProperties
        );
    }

    @Bean
    GeoLocationFacadeUtility geoLocationFacadeUtility(ResourceLoader resourceLoader) {
        return new GeoLocationFacadeUtilityImpl(
                this.ipapiGeoLocationUtility(resourceLoader),
                this.mindMaxGeoLocationUtility(resourceLoader)
        );
    }

    @Bean
    UserMetadataUtils userMetadataUtils(ResourceLoader resourceLoader) {
        return new UserMetadataUtilsImpl(
                this.geoLocationFacadeUtility(resourceLoader),
                this.userAgentDetailsUtility()
        );
    }

}
