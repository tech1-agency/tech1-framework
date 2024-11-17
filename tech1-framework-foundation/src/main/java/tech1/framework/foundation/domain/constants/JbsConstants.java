package tech1.framework.foundation.domain.constants;

import com.diogonunes.jcolor.AnsiFormat;
import lombok.experimental.UtilityClass;

import static com.diogonunes.jcolor.Attribute.*;
import static com.diogonunes.jcolor.Attribute.BOLD;

@UtilityClass
public class JbsConstants {

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

    public static class Strings {
        public static final String ACCOUNT = "ACCOUNT";
        public static final String SUM = "SUM";
        public static final String TOTAL = "TOTAL";
    }
}
