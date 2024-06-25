package io.tech1.framework.foundation.utils.impl;

import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import io.tech1.framework.foundation.domain.http.requests.UserAgentHeader;
import io.tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.foundation.utils.UserMetadataUtils;
import io.tech1.framework.foundation.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserMetadataUtilsImpl implements UserMetadataUtils {

    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final UserAgentDetailsUtility userAgentDetailsUtility;

    @Override
    public UserRequestMetadata getUserRequestMetadataProcessed(IPAddress ipAddress, UserAgentHeader userAgentHeader) {
        return UserRequestMetadata.processed(
                this.geoLocationFacadeUtility.getGeoLocation(ipAddress),
                this.userAgentDetailsUtility.getUserAgentDetails(userAgentHeader)
        );
    }
}
