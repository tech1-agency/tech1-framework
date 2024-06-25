package io.tech1.framework.foundation.utilities.geo.facades.impl;

import io.tech1.framework.foundation.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.foundation.domain.geo.GeoLocation;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import io.tech1.framework.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import io.tech1.framework.foundation.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GeoLocationFacadeUtilityImpl implements GeoLocationFacadeUtility {

    private final IPAPIGeoLocationUtility ipapiGeoLocationUtility;
    private final MindMaxGeoLocationUtility mindMaxGeoLocationUtility;

    @Override
    public GeoLocation getGeoLocation(IPAddress ipAddress) {
        try {
            return this.ipapiGeoLocationUtility.getGeoLocation(ipAddress);
        } catch (GeoLocationNotFoundException ex) {
            return this.mindMaxGeoLocationUtility.getGeoLocation(ipAddress);
        }
    }
}
