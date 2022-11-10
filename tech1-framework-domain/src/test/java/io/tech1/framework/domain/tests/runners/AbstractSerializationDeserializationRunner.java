package io.tech1.framework.domain.tests.runners;

import io.tech1.framework.domain.tests.io.TestsIOUtils;

public abstract class AbstractSerializationDeserializationRunner extends AbstractObjectMapperRunner {

    protected abstract String getFileName();

    protected abstract String getFolder();

    protected final String readFile() {
        return TestsIOUtils.readFile(this.getFolder(), this.getFileName());
    }
}
