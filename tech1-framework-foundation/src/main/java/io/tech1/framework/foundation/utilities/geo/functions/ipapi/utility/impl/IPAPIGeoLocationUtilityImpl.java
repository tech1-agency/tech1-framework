package io.tech1.framework.foundation.utilities.geo.functions.ipapi.utility.impl;

import io.tech1.framework.foundation.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.foundation.domain.geo.GeoLocation;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import io.tech1.framework.foundation.utilities.geo.facades.GeoCountryFlagUtility;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.feign.IPAPIFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IPAPIGeoLocationUtilityImpl implements IPAPIGeoLocationUtility {

    // Feign
    private final IPAPIFeign ipapiFeign;
    // Utilities
    private final GeoCountryFlagUtility geoCountryFlagUtility;

    @Override
    public GeoLocation getGeoLocation(IPAddress ipAddress) throws GeoLocationNotFoundException {
        try {
            var queryResponse = this.ipapiFeign.getIPAPIResponse(ipAddress.value());
            if (queryResponse.isSuccess()) {
                var countryCode = queryResponse.countryCode();
                var countryFlag = this.geoCountryFlagUtility.getFlagEmojiByCountryCode(countryCode);
                return GeoLocation.processed(
                        ipAddress,
                        queryResponse.country(),
                        countryCode,
                        countryFlag,
                        queryResponse.city()
                );
            } else {
                throw new GeoLocationNotFoundException(queryResponse.message());
            }
        } catch (RuntimeException throwable) {
            throw new GeoLocationNotFoundException(throwable.getMessage());
        }
    }
}
