package io.tech1.framework.foundation.utilities.geo.functions.mindmax;

import io.tech1.framework.foundation.domain.geo.GeoLocation;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;

public interface MindMaxGeoLocationUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress);
}
