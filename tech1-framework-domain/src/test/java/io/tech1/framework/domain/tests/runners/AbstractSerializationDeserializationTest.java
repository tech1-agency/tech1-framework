package io.tech1.framework.domain.tests.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.domain.tests.io.TestsIOUtils;
import lombok.SneakyThrows;

public abstract class AbstractSerializationDeserializationTest {

    protected final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected abstract String getFileName();

    protected abstract String getFolder();

    protected final String readFile() {
        return TestsIOUtils.readFile(this.getFolder(), this.getFileName());
    }

    @SneakyThrows
    protected final String writeValueAsString(Object object) {
        return OBJECT_MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
    }
}
