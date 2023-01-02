package io.tech1.framework.utilities.geo.facades.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;

@Slf4j
public class GeoCountryFlagUtilityConsoleTest extends AbstractFolderSerializationRunner {

    @Data
    static class CountryFlagFull {
        private final String name;
        private final String code;
        private final String emoji;
        private final String unicode;
    }

    @Data
    static class CountryFlagMin {
        private final String code;
        private final String emoji;
    }

    @Override
    protected String getFolder() {
        return "geo/flags";
    }

    @Test
    @Disabled
    public void readFileTest() throws JsonProcessingException {
        var flagsFullsJSON = readFile(this.getFolder(), "test-geo-countries-flags.json");
        var typeReference = new TypeReference<List<CountryFlagFull>>() {};
        var flagsFulls = OBJECT_MAPPER.readValue(flagsFullsJSON, typeReference);
        var flags = flagsFulls.stream()
                .map(flag -> new CountryFlagMin(flag.getCode(), flag.getEmoji()))
                .collect(Collectors.toList());
        var flagsJSON = OBJECT_MAPPER.writeValueAsString(flags);
        LOGGER.info(flagsJSON);
    }
}
