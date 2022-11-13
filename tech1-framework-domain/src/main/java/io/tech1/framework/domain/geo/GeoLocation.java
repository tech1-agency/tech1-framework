package io.tech1.framework.domain.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

import static io.tech1.framework.domain.constants.StringConstants.*;
import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getClientIpAddr;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasLength;

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
            String ipAddr,
            String country,
            String city,
            String exceptionDetails
    ) {
        this.ipAddr = nonNull(ipAddr) ? ipAddr : UNKNOWN;
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
            String ipAddr,
            String exceptionDetails
    ) {
        return new GeoLocation(
                ipAddr,
                UNKNOWN,
                UNKNOWN,
                exceptionDetails
        );
    }

    public static GeoLocation processing(
            HttpServletRequest httpServletRequest
    ) {
        return new GeoLocation(
                getClientIpAddr(httpServletRequest),
                UNDEFINED,
                UNDEFINED,
                EMPTY
        );
    }

    public static GeoLocation processed(
            String ipAddr,
            String country,
            String city
    ) {
        return new GeoLocation(
                ipAddr,
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
