package tech1.framework.foundation.utilities.numbers;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LongUtility {

    public static int toIntExactOrZeroOnOverflow(long value) {
        try {
            return Math.toIntExact(value);
        } catch (ArithmeticException ex) {
            return 0;
        }
    }
}
