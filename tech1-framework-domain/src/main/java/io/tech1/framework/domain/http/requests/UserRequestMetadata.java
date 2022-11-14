package io.tech1.framework.domain.http.requests;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;
import lombok.*;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.tuples.TupleExceptionDetails.exception;
import static io.tech1.framework.domain.tuples.TupleExceptionDetails.ok;
import static org.springframework.util.StringUtils.hasLength;

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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public Tuple3<String, String, String> getWhereTuple3() {
        if (this.status.isCompleted()) {
            return Tuple3.of(this.geoLocation.getIpAddr(), this.geoLocation.getCountry(), this.geoLocation.getWhere());
        } else {
            return Tuple3.of(this.geoLocation.getIpAddr(), UNDEFINED, "Processing...Please wait!");
        }
    }

    public Tuple2<String, String> getWhatTuple2() {
        if (this.status.isCompleted()) {
            return Tuple2.of(this.userAgentDetails.getBrowser(), this.userAgentDetails.getWhat());
        } else {
            return Tuple2.of(this.userAgentDetails.getBrowser(), "—");
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
