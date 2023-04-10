package io.tech1.framework.domain.constants;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class DatetimeConstants {
    public static final DateTimeFormatter DTF1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
}
