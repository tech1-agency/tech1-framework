package io.tech1.framework.foundation.utilities.geo.facades;

import io.tech1.framework.foundation.domain.geo.GeoLocation;
import io.tech1.framework.foundation.domain.http.requests.IPAddress;

public interface GeoLocationFacadeUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress);
}
