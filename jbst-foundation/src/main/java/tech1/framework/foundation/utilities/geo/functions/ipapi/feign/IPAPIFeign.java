package tech1.framework.foundation.utilities.geo.functions.ipapi.feign;

import feign.Param;
import feign.RequestLine;
import tech1.framework.foundation.utilities.geo.functions.ipapi.domain.IPAPIResponse;

public interface IPAPIFeign {
    @RequestLine("GET /json/{ipAddress}")
    IPAPIResponse getIPAPIResponse(@Param("ipAddress") String ipAddress);
}
