package io.tech1.framework.foundation.utilities.cryptography;

import io.tech1.framework.foundation.domain.tuples.Tuple2;
import lombok.experimental.UtilityClass;

import static java.util.Base64.getEncoder;

@UtilityClass
public class EncodingUtility {

    public static Tuple2<String, String> getBasicAuthenticationHeader(String username, String password) {
        return new Tuple2<>(
                "Authorization",
                getBasicAuthenticationHeaderValue(username, password)
        );
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private static String getBasicAuthenticationHeaderValue(String username, String password) {
        return "Basic " + getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
