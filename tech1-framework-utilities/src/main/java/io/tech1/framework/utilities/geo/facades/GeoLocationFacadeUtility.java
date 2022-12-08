package io.tech1.framework.utilities.geo.facades;

import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;

public interface GeoLocationFacadeUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress);
}
