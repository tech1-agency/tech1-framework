package jbst.foundation.domain.constants;

import com.diogonunes.jcolor.AnsiFormat;
import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.util.List;

import static com.diogonunes.jcolor.Attribute.*;

@UtilityClass
public class JbstConstants {

    public static class Domains {
        public static final String HARDCODED = "yyluchkiv.com";
    }

    @SuppressWarnings("unused")
    public static class JColor {
        public static final AnsiFormat BLACK_BOLD_TEXT = new AnsiFormat(BLACK_TEXT(), BOLD());
        public static final AnsiFormat BLUE_BOLD_TEXT = new AnsiFormat(BLUE_TEXT(), BOLD());
        public static final AnsiFormat GREEN_BOLD_TEXT = new AnsiFormat(GREEN_TEXT(), BOLD());
        public static final AnsiFormat RED_BOLD_TEXT = new AnsiFormat(RED_TEXT(), BOLD());
    }

    public static class MemoryUnits {
        public static final long BYTES_IN_KILOBYTE = 1024L;
        public static final long BYTES_IN_MEGABYTE = 1048576L;
        public static final long BYTES_IN_GIGABYTE = 1073741824L;
    }

    @SuppressWarnings("unused")
    public static class Strings {
        public static final String ACCOUNT = "ACCOUNT";
        public static final String SUM = "SUM";
        public static final String TOTAL = "TOTAL";
    }

    public static class Swagger {
        public static final List<String> ENDPOINTS = List.of(
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**"
        );
    }

    public static class ZoneIds {
        // Poland
        public static final ZoneId POLAND = ZoneId.of("Poland");

        // Ukraine, Kyiv
        // Daylight Saving Time; EEST: Eastern European Summer Time; UTC+3
        // Standard Time; EET: Eastern European Time; UTC+2
        // WARNING: https://github.com/eggert/tz/commit/e13e9c531fc48a04fb8d064acccc9f8ae68d5544
        public static final ZoneId UKRAINE = ZoneId.of("Europe/Kyiv");
    }
}
