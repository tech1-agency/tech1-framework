package tech1.framework.foundation.utilities.geo.functions.mindmax;

import tech1.framework.foundation.domain.geo.GeoLocation;
import tech1.framework.foundation.domain.http.requests.IPAddress;

public interface MindMaxGeoLocationUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress);
}
