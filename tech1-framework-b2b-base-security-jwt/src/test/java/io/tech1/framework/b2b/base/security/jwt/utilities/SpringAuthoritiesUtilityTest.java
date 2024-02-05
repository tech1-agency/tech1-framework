package io.tech1.framework.b2b.base.security.jwt.utilities;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.constants.SecurityJwtConstants.SUPERADMIN;
import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getResponseInvitationCodeAuthoritiesAsField;
import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.TWICE;
import static org.assertj.core.api.Assertions.assertThat;

class SpringAuthoritiesUtilityTest {

    private static Stream<Arguments> getResponseInvitationCodeAuthoritiesAsFieldArgs() {
        return Stream.of(
                Arguments.of(null, "—"),
                Arguments.of(Set.of(), "—"),
                Arguments.of(Set.of(new SimpleGrantedAuthority(INVITATION_CODE_READ), SUPERADMIN, new SimpleGrantedAuthority(INVITATION_CODE_WRITE)), "invitationCode:read, invitationCode:write, superadmin")
        );
    }

    @RepeatedTest(TWICE)
    void getSimpleGrantedAuthoritiesAsListTest() {
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

    @RepeatedTest(TWICE)
    void getSimpleGrantedAuthoritiesAsSetTest() {
        // Act
        var actual = getSimpleGrantedAuthorities(
                Set.of(
                        SUPER_ADMIN,
                        INVITATION_CODE_WRITE,
                        INVITATION_CODE_READ
                )
        );

        // Assert
        assertThat(actual).hasSize(3);
        actual.forEach(authority -> assertThat(authority).isInstanceOf(SimpleGrantedAuthority.class));
    }

    @ParameterizedTest
    @MethodSource("getResponseInvitationCodeAuthoritiesAsFieldArgs")
    void getResponseInvitationCodeAuthoritiesAsFieldTest(Set<SimpleGrantedAuthority> authorities, String expected) {
        // Act
        var actual = getResponseInvitationCodeAuthoritiesAsField(authorities);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
