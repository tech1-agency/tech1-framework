package io.tech1.framework.domain.tests.classes;

import lombok.*;

import java.time.LocalDate;

// Lombok
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ClassNestChild2 {
    private Short nest2Value1;
    private LocalDate nest2Value2;
}
