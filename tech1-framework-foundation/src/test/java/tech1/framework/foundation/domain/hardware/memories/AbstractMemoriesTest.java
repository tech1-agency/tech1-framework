package tech1.framework.foundation.domain.hardware.memories;

import tech1.framework.foundation.domain.tests.runners.AbstractFolderSerializationRunner;

public abstract class AbstractMemoriesTest extends AbstractFolderSerializationRunner {

    @Override
    protected String getFolder() {
        return "hardware/memories";
    }
}
