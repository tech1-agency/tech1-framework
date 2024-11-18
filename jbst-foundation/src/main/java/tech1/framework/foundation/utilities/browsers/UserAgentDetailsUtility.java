package tech1.framework.foundation.utilities.browsers;

import tech1.framework.foundation.domain.http.requests.UserAgentDetails;
import tech1.framework.foundation.domain.http.requests.UserAgentHeader;

public interface UserAgentDetailsUtility {
    UserAgentDetails getUserAgentDetails(UserAgentHeader userAgentHeader);
}
