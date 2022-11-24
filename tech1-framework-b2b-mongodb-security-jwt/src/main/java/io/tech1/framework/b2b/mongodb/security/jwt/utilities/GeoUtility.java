package io.tech1.framework.b2b.mongodb.security.jwt.utilities;

import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;

public interface GeoUtility {
    GeoLocation getGeoLocation(IPAddress ipAddress);
}
