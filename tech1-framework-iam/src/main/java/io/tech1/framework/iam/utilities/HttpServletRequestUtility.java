package io.tech1.framework.iam.utilities;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.nonNull;

@UtilityClass
public class HttpServletRequestUtility {

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
