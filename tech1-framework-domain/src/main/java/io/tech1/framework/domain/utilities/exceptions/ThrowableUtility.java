package io.tech1.framework.domain.utilities.exceptions;

import io.tech1.framework.domain.exceptions.ThrowableTrace;
import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;

@Deprecated(since = "use TraceUtility")
@UtilityClass
public class ThrowableUtility {

    public static ThrowableTrace getTrace(Throwable throwable) {
        return getTrace(throwable, 3000);
    }

    public static ThrowableTrace getTrace(Throwable throwable, int length) {
        return new ThrowableTrace(getTracedText(throwable, length));
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private static String getTracedText(Throwable throwable, int length) {
        // failsafe
        if (length < 200) {
            length = 200;
        }
        var message = "Throwable occurred! Please take required actions!\n\n";
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        var throwableMessage = sw.toString();
        var maxLength = Math.min(throwableMessage.length(), length);
        message += throwableMessage.substring(0, maxLength);
        return message;
    }
}
