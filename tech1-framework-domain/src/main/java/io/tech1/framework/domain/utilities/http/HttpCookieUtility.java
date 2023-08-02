package io.tech1.framework.domain.utilities.http;

import io.tech1.framework.domain.constants.StringConstants;
import io.tech1.framework.domain.exceptions.cookie.CookieNotFoundException;
import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@UtilityClass
public class HttpCookieUtility {

    public static Cookie createCookie(
            String cookieKey,
            String cookieValue,
            String domain,
            boolean httpOnly,
            int maxAge
    ) {
        var cookie = new Cookie(cookieKey, cookieValue);
        cookie.setPath(StringConstants.SLASH);
        cookie.setDomain(domain);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie createNullCookie(String cookieKey, String domain) {
        return createCookie(
                cookieKey,
                null,
                domain,
                true,
                0
        );
    }

    public static String readCookie(HttpServletRequest request, String cookieKey) throws CookieNotFoundException {
        var cookies = request.getCookies();
        if (nonNull(cookies)) {
            var cookieOpt = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieKey))
                    .findFirst();
            if (cookieOpt.isPresent()) {
                return cookieOpt.get().getValue();
            }
        }
        throw new CookieNotFoundException(entityNotFound("Cookie", cookieKey));
    }
}
