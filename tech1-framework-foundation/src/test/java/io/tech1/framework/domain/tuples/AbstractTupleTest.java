package io.tech1.framework.domain.tuples;

import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationRunner;

public abstract class AbstractTupleTest extends AbstractSerializationDeserializationRunner {

    @Override
    protected String getFolder() {
        return "tuples";
    }
}
