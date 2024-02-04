package io.tech1.framework.utilities.browsers.impl;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_UTILITIES_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.enums.Status.FAILURE;
import static io.tech1.framework.domain.enums.Status.SUCCESS;

@Slf4j
@Component
public class UserAgentDetailsUtilityImpl implements UserAgentDetailsUtility {

    private final UserAgentParser userAgentParser;
    private final boolean configured;
    private final String exceptionMessage;

    @Autowired
    public UserAgentDetailsUtilityImpl() {
        UserAgentParser userAgentParserOrNull;
        boolean configuredFlag;
        String exceptionMessageOrNull;
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        try {
            userAgentParserOrNull = new UserAgentService().loadParser(
                    List.of(
                            BrowsCapField.BROWSER,
                            BrowsCapField.PLATFORM,
                            BrowsCapField.DEVICE_TYPE
                    )
            );
            configuredFlag = true;
            exceptionMessageOrNull = null;
            LOGGER.info("{} user agent configuration status: {}", FRAMEWORK_UTILITIES_PREFIX, SUCCESS);
        } catch (ParseException | IOException ex) {
            var message = String.format("%s user agent configuration status: %s", FRAMEWORK_UTILITIES_PREFIX, FAILURE);
            LOGGER.error(message);
            LOGGER.error("Please check user agent utility configuration");
            userAgentParserOrNull = null;
            configuredFlag = false;
            exceptionMessageOrNull = ex.getMessage();
        }
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        this.userAgentParser = userAgentParserOrNull;
        this.configured = configuredFlag;
        this.exceptionMessage = exceptionMessageOrNull;
    }

    @Override
    public UserAgentDetails getUserAgentDetails(UserAgentHeader userAgentHeader) {
        if (this.configured) {
            var capabilities = this.userAgentParser.parse(userAgentHeader.getValue());
            return UserAgentDetails.processed(
                    capabilities.getBrowser(),
                    capabilities.getPlatform(),
                    capabilities.getDeviceType()
            );
        } else {
            return UserAgentDetails.unknown(this.exceptionMessage);
        }
    }
}
