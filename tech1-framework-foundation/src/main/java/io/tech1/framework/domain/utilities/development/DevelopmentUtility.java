package io.tech1.framework.domain.utilities.development;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class DevelopmentUtility {
    private static final String SEPARATOR = "===================================== %s =====================================";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void printJson(Object object, String keyword) {
        LOGGER.debug(SEPARATOR.formatted(keyword));
        try {
            LOGGER.debug("\n" + OBJECT_MAPPER.writeValueAsString(object));
        } catch (JsonProcessingException ex) {
            LOGGER.debug("Print json. EX: " + ex.getMessage());
        }
        LOGGER.debug(SEPARATOR.formatted(keyword));
    }
}
