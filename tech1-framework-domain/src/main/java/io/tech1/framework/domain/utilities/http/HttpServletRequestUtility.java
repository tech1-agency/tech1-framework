package io.tech1.framework.domain.utilities.http;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentService;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasLength;

@UtilityClass
public class HttpServletRequestUtility {

    public static String getFullURL(HttpServletRequest request) {
        if (isNull(request)) {
            throw new IllegalArgumentException(invalidAttribute("request"));
        }
        var requestURL = request.getRequestURL();
        if (isNull(requestURL)) {
            throw new IllegalArgumentException(invalidAttribute("request.requestURL"));
        }
        var queryString = request.getQueryString();
        return isNull(queryString) ? requestURL.toString() : requestURL.append("?").append(queryString).toString();
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        var unknown = "unknown";
        var ip = request.getHeader("X-Forwarded-For");
        if (!hasLength(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!hasLength(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!hasLength(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!hasLength(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static UserAgentDetails getUserAgentDetails(HttpServletRequest request) {
        try {
            var userAgentParser = new UserAgentService().loadParser(
                    List.of(
                            BrowsCapField.BROWSER,
                            BrowsCapField.PLATFORM,
                            BrowsCapField.DEVICE_TYPE
                    )
            );
            var userAgentHeader = new UserAgentHeader(request);
            var capabilities = userAgentParser.parse(userAgentHeader.getValue());
            return UserAgentDetails.processed(capabilities);
        } catch (IOException | ParseException ex) {
            return UserAgentDetails.unknown(ex.getMessage());
        }
    }
}
