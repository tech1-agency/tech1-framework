package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.GeoUtility;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
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

@Slf4j
@Component
public class GeoUtilityImpl implements GeoUtility {
    private static final String GEO_DATABASE_NAME = "GeoLite2-City.mmdb";

    private final DatabaseReader databaseReader;

    @Autowired
    public GeoUtilityImpl(ResourceLoader resourceLoader) {
        try {
            var resource = resourceLoader.getResource("classpath:" + GEO_DATABASE_NAME);
            var inputStream = resource.getInputStream();
            this.databaseReader = new DatabaseReader.Builder(inputStream).build();
            LOGGER.info("{} {} database loading status: {}", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME, SUCCESS);
        } catch (IOException | RuntimeException ex) {
            var message = String.format("%s %s database loading status: %s", FRAMEWORK_UTILITIES_PREFIX, GEO_DATABASE_NAME, FAILURE);
            LOGGER.info(message);
            LOGGER.error(LINE_SEPARATOR_INTERPUNCT);
            LOGGER.info("Please visit https://dev.maxmind.com/ to download `GeoLite2-City.mmdb` database");
            LOGGER.info("Please add `GeoLite2-City.mmdb` database to classpath");
            LOGGER.error(LINE_SEPARATOR_INTERPUNCT);
            throw new IllegalArgumentException(message + "." + ex.getMessage());
        }
    }

    @Override
    public GeoLocation getGeoLocation(IPAddress ipAddress) {
        try {
            var inetAddress = InetAddress.getByName(ipAddress.getValue());
            var response = this.databaseReader.city(inetAddress);
            var country = response.getCountry().getName();
            var city = response.getCity().getName();
            return GeoLocation.processed(
                    ipAddress,
                    country,
                    city
            );
        } catch (IOException | GeoIp2Exception ex) {
            return GeoLocation.unknown(
                    ipAddress,
                    ex.getMessage()
            );
        }
    }
}
