package io.tech1.framework.b2b.base.security.jwt.utilities;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.TWICE;
import static org.assertj.core.api.Assertions.assertThat;

class SpringAuthoritiesUtilityTest {

    @RepeatedTest(TWICE)
    void getSimpleGrantedAuthoritiesTest() {
        // Act
        var actual = getSimpleGrantedAuthorities(
                List.of(
                        SUPER_ADMIN,
                        INVITATION_CODE_WRITE,
                        INVITATION_CODE_READ
                )
        );

        // Assert
        assertThat(actual).hasSize(3);
        actual.forEach(authority -> assertThat(authority).isInstanceOf(SimpleGrantedAuthority.class));
    }

}
