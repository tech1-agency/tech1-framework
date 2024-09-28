package tech1.framework.foundation.domain.tests.classes;

import lombok.Getter;

public class ClassDefaultConstructorUnexpectedMethods {

    @Getter
    private String string;

    public void badNamingMethod1(Integer wrongParameterType1, Long wrongParameterType2) {}

    public void badNamingMethod2() {}
}
