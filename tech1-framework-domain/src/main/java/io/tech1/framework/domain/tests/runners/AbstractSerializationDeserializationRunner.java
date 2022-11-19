package io.tech1.framework.domain.tests.runners;

import io.tech1.framework.domain.tests.io.TestsIOUtils;

public abstract class AbstractSerializationDeserializationRunner extends AbstractFolderSerializationRunner {

    protected abstract String getFileName();

    protected final String readFile() {
        return TestsIOUtils.readFile(this.getFolder(), this.getFileName());
    }
}
