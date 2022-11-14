package io.tech1.framework.domain.hardware.memories;

import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;

public abstract class AbstractMemoriesTest extends AbstractFolderSerializationRunner {

    @Override
    protected String getFolder() {
        return "hardware/memories";
    }
}
