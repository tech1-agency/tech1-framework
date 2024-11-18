package jbst.foundation.domain.tests.classes;

import lombok.*;

import java.math.BigDecimal;

// Lombok
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ClassNestParent {
    private String value1;
    private Long value2;
    private int value3;
    private BigDecimal value4;
    private ClassNestChild1 child1;
    private ClassNestChild2 child2;
}
