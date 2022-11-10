package io.tech1.framework.domain.tuples;

import io.tech1.framework.domain.tests.runners.AbstractSerializationDeserializationTest;

public abstract class AbstractTupleTest extends AbstractSerializationDeserializationTest {

    @Override
    protected String getFolder() {
        return "tuples";
    }
}
