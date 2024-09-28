package tech1.framework.foundation.domain.tests.classes;

import tech1.framework.foundation.domain.base.ObjectId;
import tech1.framework.foundation.domain.plurals.Plurable;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

public record TestObject(
        ObjectId id,
        String name
) implements Plurable<ObjectId> {

    public static TestObject random() {
        return new TestObject(ObjectId.random(), randomString());
    }

    @Override
    public ObjectId getId() {
        return this.id;
    }
}
