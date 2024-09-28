package tech1.framework.foundation.utils;

import tech1.framework.foundation.domain.http.requests.IPAddress;
import tech1.framework.foundation.domain.http.requests.UserAgentHeader;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;

public interface UserMetadataUtils {
    UserRequestMetadata getUserRequestMetadataProcessed(IPAddress ipAddress, UserAgentHeader userAgentHeader);
}
