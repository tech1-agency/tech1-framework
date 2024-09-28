package tech1.framework.foundation.domain.tuples;

import tech1.framework.foundation.domain.tests.runners.AbstractSerializationDeserializationRunner;

public abstract class AbstractTupleTest extends AbstractSerializationDeserializationRunner {

    @Override
    protected String getFolder() {
        return "tuples";
    }
}
