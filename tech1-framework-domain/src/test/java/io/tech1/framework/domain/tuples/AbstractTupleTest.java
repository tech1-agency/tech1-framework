package io.tech1.framework.domain.tuples;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.domain.tests.io.TestsIOUtils;
import lombok.SneakyThrows;

public abstract class AbstractTupleTest {

    protected final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected abstract String getFileName();

    @SneakyThrows
    protected String writeValueAsString(Object object) {
        return OBJECT_MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
    }

    protected String readFile(String fileName) {
        return TestsIOUtils.readFile("tuples", fileName);
    }
}
