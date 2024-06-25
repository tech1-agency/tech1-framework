package io.tech1.framework.foundation.utilities.geo.functions.ipapi.utility;

import io.tech1.framework.foundation.domain.exceptions.geo.GeoLocationNotFoundException;
import io.tech1.framework.foundation.domain.geo.GeoLocation;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;

public interface IPAPIGeoLocationUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress) throws GeoLocationNotFoundException;
}
