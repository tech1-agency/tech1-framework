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

@Slf4j
@Component
public class GeoUtilityImpl implements GeoUtility {

    private final DatabaseReader databaseReader;

    // todo [yy] ->
    @Autowired
    public GeoUtilityImpl(ResourceLoader resourceLoader) {
        try {
            var resource = resourceLoader.getResource("classpath:GeoLite2-City.mmdb");
            var inputStream = resource.getInputStream();
            this.databaseReader = new DatabaseReader.Builder(inputStream).build();
            LOGGER.info("[Platform, Utilities] `GeoLite2-City.mmdb` database loading status: Success");
        } catch (IOException | RuntimeException ex) {
            throw new IllegalArgumentException("`GeoLite2-City.mmdb` database loading status: Failure. Exception: " + ex.getMessage());
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
