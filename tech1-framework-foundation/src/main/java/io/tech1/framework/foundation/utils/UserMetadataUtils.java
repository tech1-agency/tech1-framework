package io.tech1.framework.foundation.utils;

import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import io.tech1.framework.foundation.domain.http.requests.UserAgentHeader;
import io.tech1.framework.foundation.domain.http.requests.UserRequestMetadata;

public interface UserMetadataUtils {
    UserRequestMetadata getUserRequestMetadataProcessed(IPAddress ipAddress, UserAgentHeader userAgentHeader);
}
