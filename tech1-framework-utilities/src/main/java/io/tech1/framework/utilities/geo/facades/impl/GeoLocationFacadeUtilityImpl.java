package io.tech1.framework.utilities.geo.facades.impl;

import io.tech1.framework.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import io.tech1.framework.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import io.tech1.framework.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeoLocationFacadeUtilityImpl implements GeoLocationFacadeUtility {

    private final IPAPIGeoLocationUtility ipapiGeoLocationUtility;
    private final MindMaxGeoLocationUtility mindMaxGeoLocationUtility;

    @Override
    public GeoLocation getGeoLocation(IPAddress ipAddress) {
        try {
            return this.ipapiGeoLocationUtility.getGeoLocation(ipAddress);
        } catch (GeoLocationNotFoundException e) {
            return this.mindMaxGeoLocationUtility.getGeoLocation(ipAddress);
        }
    }
}
