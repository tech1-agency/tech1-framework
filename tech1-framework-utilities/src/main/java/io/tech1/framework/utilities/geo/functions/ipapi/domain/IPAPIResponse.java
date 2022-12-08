package io.tech1.framework.utilities.geo.functions.ipapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

// JSON
@JsonIgnoreProperties(ignoreUnknown = true)
// Lombok
@Data
public class IPAPIResponse {
    private final String status;
    private final String country;
    private final String countryCode;
    private final String city;
    private final String message;

    @JsonIgnore
    public boolean isSuccess() {
        return "success".equals(this.status);
    }
}
