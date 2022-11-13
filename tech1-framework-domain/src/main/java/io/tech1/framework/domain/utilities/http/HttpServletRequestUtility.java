package io.tech1.framework.domain.utilities.http;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.isNull;

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
}
