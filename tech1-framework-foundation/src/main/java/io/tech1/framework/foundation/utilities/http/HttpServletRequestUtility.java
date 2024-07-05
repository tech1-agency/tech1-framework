package io.tech1.framework.foundation.utilities.http;

import io.tech1.framework.foundation.domain.http.requests.IPAddress;
import lombok.experimental.UtilityClass;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.foundation.utilities.strings.StringUtility.hasLength;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@UtilityClass
public class HttpServletRequestUtility {

    public static String getBaseURL(String url) {
        int index = url.indexOf("?");
        return (index != -1) ? url.substring(0, index) : url;
    }

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

    public static IPAddress getClientIpAddr(HttpServletRequest request) {
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
        return new IPAddress(ip);
    }

    public static boolean isPOST(@NotNull HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    public static boolean isPUT(@NotNull HttpServletRequest request) {
        return "PUT".equalsIgnoreCase(request.getMethod());
    }

    public static boolean isMultipartRequest(@NotNull HttpServletRequest request) {
        var contentType = request.getContentType();
        return nonNull(contentType) && contentType.startsWith("multipart/") && (isPOST(request) || isPUT(request));
    }
}
