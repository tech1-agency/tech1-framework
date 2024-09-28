package tech1.framework.foundation.utilities.geo.facades;

import tech1.framework.foundation.domain.geo.GeoLocation;
import tech1.framework.foundation.domain.http.requests.IPAddress;

public interface GeoLocationFacadeUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress);
}
