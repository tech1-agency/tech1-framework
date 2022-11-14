package io.tech1.framework.domain.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.http.requests.IPAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.constants.StringConstants.*;
import static io.tech1.framework.domain.utilities.strings.StringUtility.hasLength;
import static java.util.Objects.nonNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class GeoLocation {
    private final String ipAddr;
    private final String country;
    @JsonIgnore
    private final String city;
    @JsonIgnore
    private final String exceptionDetails;

    private GeoLocation(
            IPAddress ipAddress,
            String country,
            String city,
            String exceptionDetails
    ) {
        this.ipAddr = nonNull(ipAddress) ? ipAddress.getValue() : UNKNOWN;
        if (nonNull(country)) {
            this.country = country.trim();
            this.city = nonNull(city) ? city.trim() : null;
        } else {
            this.country = UNKNOWN;
            this.city = null;
        }
        this.exceptionDetails = exceptionDetails;
    }

    public static GeoLocation unknown(
            IPAddress ipAddress,
            String exceptionDetails
    ) {
        return new GeoLocation(
                ipAddress,
                UNKNOWN,
                UNKNOWN,
                exceptionDetails
        );
    }

    public static GeoLocation processing(
            IPAddress ipAddress
    ) {
        return new GeoLocation(
                ipAddress,
                UNDEFINED,
                UNDEFINED,
                EMPTY
        );
    }

    public static GeoLocation processed(
            IPAddress ipAddress,
            String country,
            String city
    ) {
        return new GeoLocation(
                ipAddress,
                country,
                city,
                EMPTY
        );
    }

    public String getWhere() {
        var countryPresent = hasLength(this.country);
        var cityPresent = hasLength(this.city);
        if (countryPresent && !cityPresent) {
            return this.country;
        }
        if (countryPresent) {
            return this.country + ", " + this.city;
        }
        return UNKNOWN;
    }
}
