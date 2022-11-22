package io.tech1.framework.domain.tests.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public abstract class AbstractObjectMapperRunner {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    protected final String writeValueAsString(Object object) {
        return OBJECT_MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
    }
}
