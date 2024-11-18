package jbst.foundation.domain.tuples;

import jbst.foundation.domain.asserts.Asserts;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZoneId;

import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static jbst.foundation.utilities.time.LocalDateTimeUtility.convertTimestamp;
import static java.time.format.DateTimeFormatter.ofPattern;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class TupleSmartTimestamp {
    private final long timestamp;
    private final String formatted;

    public TupleSmartTimestamp(
            long timestamp,
            ZoneId zoneId,
            String dateTimePattern
    ) {
        Asserts.assertNonNullOrThrow(zoneId, invalidAttribute("TupleSmartTimestamp.zoneId"));
        Asserts.assertNonNullOrThrow(dateTimePattern, invalidAttribute("TupleSmartTimestamp.dateTimePattern"));
        this.timestamp = timestamp;
        this.formatted = convertTimestamp(timestamp, zoneId).format(ofPattern(dateTimePattern));
    }

    public static TupleSmartTimestamp of(
            long timestamp,
            ZoneId zoneId,
            String dateTimePattern
    ) {
        return new TupleSmartTimestamp(
                timestamp,
                zoneId,
                dateTimePattern
        );
    }
}
