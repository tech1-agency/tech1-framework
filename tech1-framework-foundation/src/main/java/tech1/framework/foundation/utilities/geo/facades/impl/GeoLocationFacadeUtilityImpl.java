package tech1.framework.foundation.utilities.geo.facades.impl;

import tech1.framework.foundation.domain.exceptions.geo.GeoLocationNotFoundException;
import tech1.framework.foundation.domain.geo.GeoLocation;
import tech1.framework.foundation.domain.http.requests.IPAddress;
import tech1.framework.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
import tech1.framework.foundation.utilities.geo.functions.ipapi.utility.IPAPIGeoLocationUtility;
import tech1.framework.foundation.utilities.geo.functions.mindmax.MindMaxGeoLocationUtility;
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
