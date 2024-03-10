package io.tech1.framework.domain.tests.classes;

import io.tech1.framework.domain.base.ObjectId;
import io.tech1.framework.domain.plurals.Plurable;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record TestObject(
        ObjectId id,
        String name
) implements Plurable<ObjectId> {

    public static TestObject random() {
        return new TestObject(ObjectId.random(), randomString());
    }
}
