package io.tech1.framework.domain.tuples;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZoneId;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.time.LocalDateTimeUtility.convertTimestamp;
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
        assertNonNullOrThrow(zoneId, invalidAttribute("SmartTimestamp.zoneId"));
        assertNonNullOrThrow(dateTimePattern, invalidAttribute("SmartTimestamp.dateTimePattern`"));

        var ldt = convertTimestamp(timestamp, zoneId);
        var formatter = ofPattern(dateTimePattern);
        var formatted  = ldt.format(formatter);

        this.timestamp = timestamp;
        this.formatted = formatted;
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
