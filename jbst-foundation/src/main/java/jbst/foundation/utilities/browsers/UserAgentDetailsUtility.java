package jbst.foundation.utilities.browsers;

import jbst.foundation.domain.http.requests.UserAgentDetails;
import jbst.foundation.domain.http.requests.UserAgentHeader;

public interface UserAgentDetailsUtility {
    UserAgentDetails getUserAgentDetails(UserAgentHeader userAgentHeader);
}
