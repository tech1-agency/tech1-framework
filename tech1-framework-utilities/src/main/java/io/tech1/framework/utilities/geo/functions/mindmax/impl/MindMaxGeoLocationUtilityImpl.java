package io.tech1.framework.utilities.geo.functions.mindmax.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.utilities.printer.PRINTER;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.utilities.geo.facades.GeoCountryFlagUtility;
import io.tech1.framework.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_UTILITIES_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.enums.Status.FAILURE;
import static io.tech1.framework.domain.enums.Status.SUCCESS;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;

@Slf4j
@Component
public class MindMaxGeoLocationUtilityImpl implements MindMaxGeoLocationUtility {
    private static final String GEO_DATABASE_NAME = "GeoLite2-City.mmdb";

    // Database
    private final DatabaseReader databaseReader;
    // Utilities
    private final GeoCountryFlagUtility geoCountryFlagUtility;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Autowired
    public MindMaxGeoLocationUtilityImpl(
            ResourceLoader resourceLoader,
            GeoCountryFlagUtility geoCountryFlagUtility,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        this.geoCountryFlagUtility = geoCountryFlagUtility;
        this.applicationFrameworkProperties = applicationFrameworkProperties;
        PRINTER.info(LINE_SEPARATOR_INTERPUNCT);
        if (applicationFrameworkProperties.getUtilitiesConfigs().getGeoLocationsConfigs().isGeoLiteCityDatabaseEnabled()) {
            try {
                PRINTER.info("{} Geo location {} database is enabled", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME);
                var resource = resourceLoader.getResource("classpath:" + GEO_DATABASE_NAME);
                var inputStream = resource.getInputStream();
                this.databaseReader = new DatabaseReader.Builder(inputStream).build();
                PRINTER.info("{} Geo location {} database configuration status: {}", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME, SUCCESS);
            } catch (IOException | RuntimeException ex) {
                PRINTER.error("%s Geo location %s database loading status: %s".formatted(FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME, FAILURE));
                PRINTER.error("Please make sure {} database is in classpath", GEO_DATABASE_NAME);
                throw new IllegalArgumentException(ex.getMessage());
            }
        } else {
            PRINTER.info("{} Geo location {} database is disabled", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME);
            this.databaseReader = null;
        }
        PRINTER.info(LINE_SEPARATOR_INTERPUNCT);
    }

    @Override
    public GeoLocation getGeoLocation(IPAddress ipAddress) {
        if (!this.applicationFrameworkProperties.getUtilitiesConfigs().getGeoLocationsConfigs().isGeoLiteCityDatabaseEnabled()) {
            return GeoLocation.unknown(ipAddress, contactDevelopmentTeam("Geo configurations failure"));
        }
        try {
            var inetAddress = InetAddress.getByName(ipAddress.value());
            var response = this.databaseReader.city(inetAddress);
            var countryCode = response.getCountry().getIsoCode();
            var countryFlag = this.geoCountryFlagUtility.getFlagEmojiByCountryCode(countryCode);
            return GeoLocation.processed(
                    ipAddress,
                    response.getCountry().getName(),
                    countryCode,
                    countryFlag,
                    response.getCity().getName()
            );
        } catch (IOException | GeoIp2Exception ex) {
            return GeoLocation.unknown(ipAddress, ex.getMessage());
        }
    }
}
