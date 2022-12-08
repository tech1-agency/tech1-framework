package io.tech1.framework.domain.geo;

import lombok.Data;

// Lombok
@Data
public class GeoCountryFlag {
    private final String name;
    private final String code;
    private final String emoji;
    private final String unicode;
}
