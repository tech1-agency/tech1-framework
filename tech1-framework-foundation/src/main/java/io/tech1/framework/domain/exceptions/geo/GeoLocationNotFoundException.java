package io.tech1.framework.domain.exceptions.geo;

public class GeoLocationNotFoundException extends Exception {

    public GeoLocationNotFoundException(String message) {
        super("Geo location not found: " + message);
    }
}
