package io.tech1.framework.foundation.utilities.cryptography;

import io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility;
import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static io.tech1.framework.foundation.domain.asserts.Asserts.assertNonNullOrThrow;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.codec.binary.Hex.encodeHexString;

@UtilityClass
public class HashingUtility {
    private static final String EXCEPTION_MESSAGE = "Hashing Failure. Value: `%s`. Key: `%s`. Algorithm: `%s`. Exception: `%s`";

    public static String hmacSha256(String value, String hashingKey) {
        return shaByAlgorithm(value, hashingKey, "HmacSHA256");
    }

    public static String hmacSha384(String value, String hashingKey) {
        return shaByAlgorithm(value, hashingKey, "HmacSHA384");
    }

    public static String hmacSha512(String value, String hashingKey) {
        return shaByAlgorithm(value, hashingKey, "HmacSHA512");
    }

    public static String shaByAlgorithm(String value, String hashingKey, String algorithm) {
        assertNonNullOrThrow(value, ExceptionsMessagesUtility.invalidAttribute("value"));
        assertNonNullOrThrow(hashingKey, ExceptionsMessagesUtility.invalidAttribute("hashingKey"));
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
