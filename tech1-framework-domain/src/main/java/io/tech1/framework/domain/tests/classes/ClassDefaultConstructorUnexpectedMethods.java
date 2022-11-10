package io.tech1.framework.domain.tests.classes;

import lombok.Getter;

public class ClassDefaultConstructorUnexpectedMethods {

    @Getter
    private String string;

    public void badNamingMethod1(Integer wrongParameterType1, Long wrongParameterType2) {}

    public void badNamingMethod2() {}
}
