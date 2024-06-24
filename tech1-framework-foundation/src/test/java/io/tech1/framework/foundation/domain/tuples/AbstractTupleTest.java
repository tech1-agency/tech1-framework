package io.tech1.framework.foundation.domain.tuples;

import io.tech1.framework.foundation.domain.tests.runners.AbstractSerializationDeserializationRunner;

public abstract class AbstractTupleTest extends AbstractSerializationDeserializationRunner {

    @Override
    protected String getFolder() {
        return "tuples";
    }
}
