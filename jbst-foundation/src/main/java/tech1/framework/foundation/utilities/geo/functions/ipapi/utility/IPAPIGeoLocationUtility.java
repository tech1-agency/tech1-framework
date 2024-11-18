package tech1.framework.foundation.utilities.geo.functions.ipapi.utility;

import tech1.framework.foundation.domain.exceptions.geo.GeoLocationNotFoundException;
import tech1.framework.foundation.domain.geo.GeoLocation;
import tech1.framework.foundation.domain.http.requests.IPAddress;

public interface IPAPIGeoLocationUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress) throws GeoLocationNotFoundException;
}
