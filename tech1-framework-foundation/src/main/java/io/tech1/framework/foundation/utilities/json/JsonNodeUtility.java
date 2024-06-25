package io.tech1.framework.foundation.utilities.json;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

import static java.util.Objects.isNull;

@UtilityClass
public class JsonNodeUtility {

    public static BigDecimal getJsonNodeValueAsBigDecimalOrZero(JsonNode jsonNode) {
        if (isNull(jsonNode)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(jsonNode.textValue());
    }

    public BigDecimal getJsonNodeFieldValueAsBigDecimalOrZero(JsonNode jsonNode, String fieldName) {
        if (isNull(jsonNode)) {
            return BigDecimal.ZERO;
        }
        var fieldJN = jsonNode.get(fieldName);
        if (isNull(fieldJN)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(fieldJN.textValue());
    }
}
