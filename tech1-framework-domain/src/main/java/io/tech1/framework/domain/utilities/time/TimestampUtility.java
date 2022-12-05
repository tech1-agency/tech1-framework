package io.tech1.framework.domain.utilities.time;

import io.tech1.framework.domain.time.TimeAmount;
import io.tech1.framework.domain.tuples.Tuple2;
import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import static io.tech1.framework.domain.utilities.time.LocalDateTimeUtility.getTimestamp;

@UtilityClass
public class TimestampUtility {
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static long getCurrentMonthAtStartOfMonthAndAtStartOfDayTimestampUTC() {
        return getCurrentMonthAtStartOfMonthAndAtStartOfDayTimestamp(ZoneOffset.UTC);
    }

    public static long getCurrentMonthAtStartOfMonthAndAtStartOfDayTimestamp(ZoneId zoneId) {
        return getTimestamp(
                LocalDateUtility.now(zoneId).withDayOfMonth(1).atStartOfDay(),
                zoneId
        );
    }

    public static long getPreviousMonthAtStartOfMonthAndAtStartOfDayTimestampUTC() {
        return getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestampUTC(1);
    }

    public static long getPreviousMonthAtStartOfMonthAndAtStartOfDayTimestamp(ZoneId zoneId) {
        return getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestamp(zoneId, 1);
    }

    public static long getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestampUTC(int monthAgo) {
        return getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestamp(ZoneOffset.UTC, monthAgo);
    }

    public static long getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestamp(ZoneId zoneId, int monthAgo) {
        return getTimestamp(
                LocalDateUtility.now(zoneId).minusMonths(monthAgo).withDayOfMonth(1).atStartOfDay(),
                zoneId
        );
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
