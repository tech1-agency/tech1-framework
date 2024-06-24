package io.tech1.framework.domain.tests.classes;

import io.tech1.framework.domain.base.ObjectId;
import io.tech1.framework.domain.plurals.Plurals;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Lombok
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class TestObjects extends Plurals<TestObject, ObjectId> {

    public TestObjects(List<TestObject> values) {
        super(values);
    }

    public static TestObjects random(int size) {
        return new TestObjects(
                IntStream.range(0, size)
                        .mapToObj(i -> TestObject.random())
                        .toList()
        );
    }

    public Set<String> getNames() {
        return this.values.stream().map(TestObject::name).collect(Collectors.toSet());
    }
}
