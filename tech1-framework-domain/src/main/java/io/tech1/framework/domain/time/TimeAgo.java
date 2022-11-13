package io.tech1.framework.domain.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.domain.tuples.Tuple2;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

import static io.tech1.framework.domain.utilities.strings.StringUtility.isNullOrBlank;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.util.concurrent.TimeUnit.*;

// Lombok
@Getter
@EqualsAndHashCode
public class TimeAgo {
    public static final List<Tuple2<Long, String>> UNITS = List.of(
            Tuple2.of(DAYS.toMillis(365), "year"),
            Tuple2.of(DAYS.toMillis(30), "month"),
            Tuple2.of(DAYS.toMillis(1), "day"),
            Tuple2.of(HOURS.toMillis(1), "hour"),
            Tuple2.of(MINUTES.toMillis(1), "minute"),
            Tuple2.of(SECONDS.toMillis(1), "second")
    );

    @JsonValue
    private final String value;

    @JsonCreator
    public TimeAgo(long timestamp) {
        var duration = getCurrentTimestamp() - timestamp;
        var agoSb = new StringBuilder();
        for (Tuple2<Long, String> unit : UNITS) {
            var current = unit.getA();
            var temp = duration / current;
            if (temp > 0) {
                agoSb.append(temp);
                agoSb.append(" ");
                agoSb.append(unit.getB());
                agoSb.append(temp != 1 ? "s" : "");
                agoSb.append(" ");
                agoSb.append("ago");
                break;
            }
        }
        var ago = agoSb.toString();
        if(isNullOrBlank(ago)) {
            this.value = "just now";
        } else {
            this.value = ago;
        }
    }

    public static TimeAgo of(long timestamp) {
        return new TimeAgo(timestamp);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
