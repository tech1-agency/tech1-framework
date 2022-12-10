package io.tech1.framework.utilities.geo.functions.mindmax;

import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;

public interface MindMaxGeoLocationUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress);
}
