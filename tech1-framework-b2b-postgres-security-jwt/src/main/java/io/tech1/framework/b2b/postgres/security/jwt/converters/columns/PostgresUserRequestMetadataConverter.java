package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.SneakyThrows;

import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class PostgresUserRequestMetadataConverter extends AbstractAttributeConverter<UserRequestMetadata, String> {

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(UserRequestMetadata metadata) {
        Map<String, String> json = new HashMap<>();
        var geoLocation = metadata.getGeoLocation();
        var userAgentDetails = metadata.getUserAgentDetails();
        json.put("status", metadata.getStatus().name());
        json.put("ipAddr", geoLocation.getIpAddr());
        json.put("country", geoLocation.getCountry());
        json.put("countryCode", geoLocation.getCountryCode());
        json.put("countryFlag", geoLocation.getCountryFlag());
        json.put("city", geoLocation.getCity());
        json.put("geoLocationExceptionDetails", geoLocation.getExceptionDetails());
        json.put("browser", userAgentDetails.getBrowser());
        json.put("platform", userAgentDetails.getPlatform());
        json.put("deviceType", userAgentDetails.getDeviceType());
        json.put("userAgentDetailsExceptionDetails", userAgentDetails.getExceptionDetails());
        return OBJECT_MAPPER.writeValueAsString(json);
    }

    @SneakyThrows
    @Override
    public UserRequestMetadata convertToEntityAttribute(String value) {
        var typeReference = new TypeReference<Map<String, String>>() {};
        var json = OBJECT_MAPPER.readValue(value, typeReference);
        return new UserRequestMetadata(
                Status.valueOf(json.get("status")),
                new GeoLocation(
                        json.get("ipAddr"),
                        json.get("country"),
                        json.get("countryCode"),
                        json.get("countryFlag"),
                        json.get("city"),
                        json.get("geoLocationExceptionDetails")
                ),
                new UserAgentDetails(
                        json.get("browser"),
                        json.get("platform"),
                        json.get("deviceType"),
                        json.get("userAgentDetailsExceptionDetails")
                )
        );
    }
}
