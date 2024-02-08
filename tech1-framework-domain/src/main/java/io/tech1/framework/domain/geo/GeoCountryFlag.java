package io.tech1.framework.domain.geo;

public record GeoCountryFlag(
        String name,
        String code,
        String emoji,
        String unicode
) {
    public static GeoCountryFlag unknown() {
        return new GeoCountryFlag("Unknown", "Unknown", "🏴‍", "—");
    }
}
