package jbst.foundation.utils.impl;

import jbst.foundation.domain.http.requests.IPAddress;
import jbst.foundation.domain.http.requests.UserAgentHeader;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import jbst.foundation.utils.UserMetadataUtils;
import jbst.foundation.utilities.browsers.UserAgentDetailsUtility;
import jbst.foundation.utilities.geo.facades.GeoLocationFacadeUtility;
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
