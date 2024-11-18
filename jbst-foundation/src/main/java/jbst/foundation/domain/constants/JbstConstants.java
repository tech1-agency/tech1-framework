package jbst.foundation.domain.constants;

import com.diogonunes.jcolor.AnsiFormat;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.diogonunes.jcolor.Attribute.*;

@UtilityClass
public class JbstConstants {

    public class BigDecimals {
        public static final BigDecimal MINUS_ONE = BigDecimal.valueOf(-1);
        public static final BigDecimal TWO = BigDecimal.valueOf(2L);
        public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    }

    @SuppressWarnings("unused")
    public class BigIntegers {
        public static final BigInteger MINUS_ONE = BigInteger.valueOf(-1);
        public static final BigInteger ONE_HUNDRED = BigInteger.valueOf(100);
    }

    @SuppressWarnings("unused")
    public class DateTimeFormatters {
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

    public static class Domains {
        public static final String HARDCODED = "yyluchkiv.com";
    }

    @SuppressWarnings("unused")
    public class Dropdowns {
        public static final String ALL = "All";
    }

    @SuppressWarnings("unused")
    public static class JColor {
        public static final AnsiFormat BLACK_BOLD_TEXT = new AnsiFormat(BLACK_TEXT(), BOLD());
        public static final AnsiFormat BLUE_BOLD_TEXT = new AnsiFormat(BLUE_TEXT(), BOLD());
        public static final AnsiFormat GREEN_BOLD_TEXT = new AnsiFormat(GREEN_TEXT(), BOLD());
        public static final AnsiFormat RED_BOLD_TEXT = new AnsiFormat(RED_TEXT(), BOLD());
    }

    public class Files {
        public static final String PATH_DELIMITER = "/";
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

        public static final String UNKNOWN = "Unknown";

        public static final String OPS = "[Ops]";
    }

    public static class Swagger {
        public static final List<String> ENDPOINTS = List.of(
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**"
        );
    }

    public static class Symbols {
        public static final String COMMA = ",";
        public static final String DASH = "—";
        public static final String EMPTY = "";
        public static final String HYPHEN = "-";
        public static final String SEMICOLON = ";";
        public static final String SLASH = "/";
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
