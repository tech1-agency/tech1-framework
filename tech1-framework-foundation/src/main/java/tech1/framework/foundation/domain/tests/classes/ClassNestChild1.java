package tech1.framework.foundation.domain.tests.classes;

import tech1.framework.foundation.domain.tests.enums.EnumUnderTests;
import lombok.*;

import java.math.BigDecimal;

// Lombok
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ClassNestChild1 {
    private Integer nest1Value1;
    private BigDecimal nest1Value2;
    private EnumUnderTests nest1Value3;
}
