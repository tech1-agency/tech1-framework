package jbst.foundation.domain.tests.runners;

import jbst.foundation.domain.tests.io.TestsIOUtils;

public abstract class AbstractSerializationDeserializationRunner extends AbstractFolderSerializationRunner {

    protected abstract String getFileName();

    protected final String readFile() {
        return TestsIOUtils.readFile(this.getFolder(), this.getFileName());
    }
}
