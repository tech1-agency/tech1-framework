package io.tech1.framework.foundation.utilities.browsers;

import io.tech1.framework.foundation.domain.http.requests.UserAgentDetails;
import io.tech1.framework.foundation.domain.http.requests.UserAgentHeader;

public interface UserAgentDetailsUtility {
    UserAgentDetails getUserAgentDetails(UserAgentHeader userAgentHeader);
}
