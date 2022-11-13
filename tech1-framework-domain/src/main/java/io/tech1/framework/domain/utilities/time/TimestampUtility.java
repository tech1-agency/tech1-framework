package io.tech1.framework.domain.utilities.time;

import io.tech1.framework.domain.time.TimeAmount;
import io.tech1.framework.domain.tuples.Tuple2;
import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class TimestampUtility {
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static Tuple2<Long, Long> getPastRange(TimeAmount timeAmount) {
        var currentTimestamp = getCurrentTimestamp();
        var past = currentTimestamp - timeAmount.toMillis();
        return Tuple2.of(past, currentTimestamp);
    }

    public static Tuple2<Long, Long> getFutureRange(TimeAmount timeAmount) {
        var currentTimestamp = getCurrentTimestamp();
        var future = currentTimestamp + timeAmount.toMillis();
        return Tuple2.of(currentTimestamp, future);
    }

    public static boolean isBetween(long timestamp, long past, long future) {
        return timestamp > past && timestamp < future;
    }

    public static boolean isPast(long timestamp) {
        return getCurrentTimestamp() > timestamp;
    }

    public static boolean isFuture(long timestamp) {
        return getCurrentTimestamp() < timestamp;
    }

    public boolean isCurrentTimestampNSecondsMore(long timestamp, long seconds) {
        return TimeUnit.MILLISECONDS.toSeconds(getCurrentTimestamp() - timestamp) > seconds;
    }
}
