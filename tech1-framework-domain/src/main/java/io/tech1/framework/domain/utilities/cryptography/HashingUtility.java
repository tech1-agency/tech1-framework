package io.tech1.framework.domain.utilities.cryptography;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.codec.binary.Hex.encodeHexString;

@UtilityClass
public class HashingUtility {
    private static final String EXCEPTION_MESSAGE = "Hashing Failure. Value: `%s`, Hashing Key: `%s`, Algorithm: '%s', Exception: '%s'";

    private static final String ASSERTION_VALUE_MESSAGE = "Hashing Algorithm: '%s'. Argument 'value' is required";
    private static final String ASSERTION_HASHING_KEY_MESSAGE = "Hashing Algorithm: '%s'. Argument 'hashingKey' is required";

    private static final String SHA_256 = "HmacSHA256";
    private static final String SHA_384 = "HmacSHA384";
    private static final String SHA_512 = "HmacSHA512";

    public static String hmacSha256(String value, String hashingKey) {
        return shaByAlgorithm(value, hashingKey, SHA_256);
    }

    public static String hmacSha384(String value, String hashingKey) {
        return shaByAlgorithm(value, hashingKey, SHA_384);
    }

    public static String hmacSha512(String value, String hashingKey) {
        return shaByAlgorithm(value, hashingKey, SHA_512);
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private static String shaByAlgorithm(String value, String hashingKey, String algorithm) {
        assertNonNullOrThrow(value, String.format(ASSERTION_VALUE_MESSAGE, algorithm));
        assertNonNullOrThrow(hashingKey, String.format(ASSERTION_HASHING_KEY_MESSAGE, algorithm));
        try {
            var mac = Mac.getInstance(algorithm);
            var keySpec = new SecretKeySpec(hashingKey.getBytes(UTF_8), algorithm);
            mac.init(keySpec);
            return encodeHexString(mac.doFinal(value.getBytes(UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            var message = String.format(
                    EXCEPTION_MESSAGE,
                    value,
                    hashingKey,
                    algorithm,
                    ex.getClass().getSimpleName()
            );
            throw new IllegalArgumentException(message);
        }
    }
}
