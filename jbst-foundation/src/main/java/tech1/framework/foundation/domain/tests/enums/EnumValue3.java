package tech1.framework.foundation.domain.tests.enums;

import tech1.framework.foundation.domain.enums.EnumValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EnumValue3 implements EnumValue<Integer> {
    EMAIL_SENT(0),
    CANCELLED(1),
    AWAITING_APPROVAL(2),
    REJECTED(3),
    PROCESSING(4),
    FAILURE(5),
    COMPLETED(6),
    UNKNOWN(-1);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
