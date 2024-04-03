package io.tech1.framework.domain.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.tests.constants.TestsFlagsConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.constants.StringConstants.*;
import static io.tech1.framework.domain.http.requests.IPAddress.localhost;
import static io.tech1.framework.domain.tests.constants.TestsFlagsConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static io.tech1.framework.domain.utilities.strings.StringUtility.hasLength;
import static java.util.Objects.nonNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class GeoLocation {
    private final String ipAddr;
    private final String country;
    private final String countryCode;
    private final String countryFlag;
    @JsonIgnore
    private final String city;
    @JsonIgnore
    private final String exceptionDetails;

    public GeoLocation(
            String ipAddr,
            String country,
            String countryCode,
            String countryFlag,
            String city,
            String exceptionDetails
    ) {
        this.ipAddr = ipAddr;
        if (nonNull(countryCode)) {
            this.countryCode = countryCode;
        } else {
            this.countryCode = UNKNOWN;
        }
        if (nonNull(countryFlag)) {
            this.countryFlag = countryFlag;
        } else {
            this.countryFlag = TestsFlagsConstants.UNKNOWN;
        }
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
                getIpAddrOrUnknown(ipAddress),
                UNKNOWN,
                UNKNOWN,
                TestsFlagsConstants.UNKNOWN,
                UNKNOWN,
                exceptionDetails
        );
    }

    public static GeoLocation processing(
            IPAddress ipAddress
    ) {
        return new GeoLocation(
                getIpAddrOrUnknown(ipAddress),
                UNDEFINED,
                UNDEFINED,
                TestsFlagsConstants.UNKNOWN,
                UNDEFINED,
                EMPTY
        );
    }

    public static GeoLocation processed(
            IPAddress ipAddress,
            String country,
            String countryCode,
            String countryFlag,
            String city
    ) {
        return new GeoLocation(
                getIpAddrOrUnknown(ipAddress),
                country,
                countryCode,
                countryFlag,
                city,
                EMPTY
        );
    }

    public static GeoLocation valid() {
        return GeoLocation.processed(
                localhost(),
                "Ukraine",
                "UA",
                "🇺🇦",
                "Lviv"
        );
    }

    public static GeoLocation invalid() {
        return GeoLocation.unknown(
                localhost(),
                "Location is unknown"
        );
    }

    public static GeoLocation random() {
        return randomBoolean() ? valid() : invalid();
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

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    public static String getIpAddrOrUnknown(IPAddress ipAddress) {
        return nonNull(ipAddress) ? ipAddress.value() : UNKNOWN;
    }
}
