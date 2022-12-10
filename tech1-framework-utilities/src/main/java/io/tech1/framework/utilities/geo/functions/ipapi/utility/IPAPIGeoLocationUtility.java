package io.tech1.framework.utilities.geo.functions.ipapi.utility;

import io.tech1.framework.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;

public interface IPAPIGeoLocationUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress) throws GeoLocationNotFoundException;
}
