package jbst.foundation.utils;

import jbst.foundation.domain.http.requests.IPAddress;
import jbst.foundation.domain.http.requests.UserAgentHeader;
import jbst.foundation.domain.http.requests.UserRequestMetadata;

public interface UserMetadataUtils {
    UserRequestMetadata getUserRequestMetadataProcessed(IPAddress ipAddress, UserAgentHeader userAgentHeader);
}
