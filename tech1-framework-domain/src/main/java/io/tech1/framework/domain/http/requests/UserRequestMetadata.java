package io.tech1.framework.domain.http.requests;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.tests.constants.TestsFlagsConstants;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.tuples.TupleExceptionDetails.exception;
import static io.tech1.framework.domain.tuples.TupleExceptionDetails.ok;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.pleaseWait;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static io.tech1.framework.domain.utilities.strings.StringUtility.hasLength;

// JSON
@JsonPropertyOrder({
        "status",
        "geoLocation",
        "userAgentDetails",
        "whereTuple3",
        "whatTuple2",
        "exception"
})
// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class UserRequestMetadata {
    private final Status status;
    private final GeoLocation geoLocation;
    private final UserAgentDetails userAgentDetails;

    public static UserRequestMetadata processing(
            IPAddress ipAddress
    ) {
        return new UserRequestMetadata(
                Status.STARTED,
                GeoLocation.processing(ipAddress),
                UserAgentDetails.processing()
        );
    }

    public static UserRequestMetadata processed(
            GeoLocation geoLocation,
            UserAgentDetails userAgentDetails
    ) {
        return new UserRequestMetadata(
                Status.COMPLETED,
                geoLocation,
                userAgentDetails
        );
    }

    public static UserRequestMetadata valid() {
        return UserRequestMetadata.processed(
                GeoLocation.valid(),
                UserAgentDetails.valid()
        );
    }

    public static UserRequestMetadata invalid() {
        return UserRequestMetadata.processed(
                GeoLocation.invalid(),
                UserAgentDetails.invalid()
        );
    }

    public static UserRequestMetadata random() {
        return randomBoolean() ? valid() : invalid();
    }

    public Tuple3<String, String, String> getWhereTuple3() {
        if (this.status.isCompleted()) {
            return new Tuple3<>(this.geoLocation.getIpAddr(), this.geoLocation.getCountryFlag(), this.geoLocation.getWhere());
        } else {
            return new Tuple3<>(this.geoLocation.getIpAddr(), TestsFlagsConstants.UNKNOWN, pleaseWait("Processing"));
        }
    }

    public Tuple2<String, String> getWhatTuple2() {
        if (this.status.isCompleted()) {
            return new Tuple2<>(this.userAgentDetails.getBrowser(), this.userAgentDetails.getWhat());
        } else {
            return new Tuple2<>(this.userAgentDetails.getBrowser(), "—");
        }
    }

    public TupleExceptionDetails getException() {
        var geoExceptionDetails = this.geoLocation.getExceptionDetails();
        var userAgentExceptionDetails = this.userAgentDetails.getExceptionDetails();
        if (hasLength(geoExceptionDetails) && hasLength(userAgentExceptionDetails)) {
            return exception(geoExceptionDetails + ". " + userAgentExceptionDetails);
        }
        if (hasLength(geoExceptionDetails)) {
            return exception(geoExceptionDetails);
        }
        if (hasLength(userAgentExceptionDetails)) {
            return exception(userAgentExceptionDetails);
        }
        return ok();
    }
}
