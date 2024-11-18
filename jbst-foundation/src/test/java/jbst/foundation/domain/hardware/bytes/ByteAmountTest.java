package jbst.foundation.domain.hardware.bytes;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ByteAmountTest {

    @Test
    void ofGbTest() {
        // Act
        var actual = ByteAmount.ofGb(1573741824L);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getUnit()).isEqualTo(ByteUnit.GIGABYTE);
        assertThat(actual.getAmount()).isEqualTo(new BigDecimal("1.4657"));
    }

    @Test
    void ofMBTest() {
        // Act
        var actual = ByteAmount.ofMB(1573741824L);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getUnit()).isEqualTo(ByteUnit.MEGABYTE);
        assertThat(actual.getAmount()).isEqualTo(new BigDecimal("1500.8"));
    }
}
