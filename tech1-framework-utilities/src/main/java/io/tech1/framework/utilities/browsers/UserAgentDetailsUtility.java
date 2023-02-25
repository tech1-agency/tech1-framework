package io.tech1.framework.utilities.browsers;

import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserAgentHeader;

public interface UserAgentDetailsUtility {
    UserAgentDetails getUserAgentDetails(UserAgentHeader userAgentHeader);
}
