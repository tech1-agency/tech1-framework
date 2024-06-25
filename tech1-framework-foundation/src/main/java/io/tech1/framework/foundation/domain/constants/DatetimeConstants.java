package io.tech1.framework.foundation.domain.constants;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class DatetimeConstants {
    public static final DateTimeFormatter DTF10 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
    public static final DateTimeFormatter DTF11 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final DateTimeFormatter DTF12 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static final DateTimeFormatter DTF13 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final DateTimeFormatter DTF20 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter DTF21 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DTF22 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DTF23 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter DTF30 = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSS");
    public static final DateTimeFormatter DTF31 = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    public static final DateTimeFormatter DTF32 = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
    public static final DateTimeFormatter DTF33 = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public static final DateTimeFormatter DTF41 = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    public static final DateTimeFormatter DTF42 = DateTimeFormatter.ofPattern("dd MMMM");

    public static final DateTimeFormatter DTF51 = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DTF52 = DateTimeFormatter.ofPattern("HH:mm");
}
