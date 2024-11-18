package jbst.foundation.domain.tests.classes;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClassDefaultConstructorUnexpectedSetter {

    @Getter
    private String string;

    public void setException1(Integer wrongParameterType1, Long wrongParameterType2) {}

    public void setException2() {}
}
