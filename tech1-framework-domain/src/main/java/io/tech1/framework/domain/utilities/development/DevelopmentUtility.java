package io.tech1.framework.domain.utilities.development;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class DevelopmentUtility {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void printJson(String prefix, Object object) {
        LOGGER.warn(prefix + ": =====================================================================================");
        try {
            LOGGER.warn(OBJECT_MAPPER.writeValueAsString(object));
        } catch (JsonProcessingException ex) {
            LOGGER.error("Print json. EX: " + ex.getMessage());
        }
        LOGGER.warn(prefix + ": =====================================================================================");
    }
}
