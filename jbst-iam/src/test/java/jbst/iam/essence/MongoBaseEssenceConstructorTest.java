package jbst.iam.essence;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import tech1.framework.foundation.domain.properties.base.DefaultUser;
import jbst.iam.repositories.mongodb.MongoInvitationCodesRepository;
import jbst.iam.repositories.mongodb.MongoUsersRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static tech1.framework.foundation.utilities.random.EntityUtility.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoBaseEssenceConstructorTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesTestsHardcodedContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        MongoInvitationCodesRepository invitationCodeRepository() {
            return mock(MongoInvitationCodesRepository.class);
        }

        @Bean
        MongoUsersRepository userRepository() {
            return mock(MongoUsersRepository.class);
        }

        @Bean
        EssenceConstructor essenceConstructor() {
            return new MongoBaseEssenceConstructor(
                    this.invitationCodeRepository(),
                    this.userRepository(),
                    this.applicationFrameworkProperties
            );
        }
    }

    private final MongoInvitationCodesRepository invitationCodesRepository;
    private final MongoUsersRepository usersRepository;

    private final EssenceConstructor componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationCodesRepository,
                this.usersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationCodesRepository,
                this.usersRepository
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void saveDefaultUsersTest() {
        // Arrange
        var defaultUsers = list345(DefaultUser.class);

        // Act
        var actual = this.componentUnderTest.saveDefaultUsers(defaultUsers);

        // Assert
        var userAC = ArgumentCaptor.forClass(List.class);
        verify(this.usersRepository).saveAll(userAC.capture());
        assertThat(actual)
                .isEqualTo(defaultUsers.size())
                .isEqualTo(userAC.getValue().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void saveInvitationCodesTest() {
        // Arrange
        var defaultUser = entity(DefaultUser.class);
        var authorities = set345(SimpleGrantedAuthority.class);

        // Act
        this.componentUnderTest.saveInvitationCodes(defaultUser, authorities);

        // Assert
        var userAC = ArgumentCaptor.forClass(List.class);
        verify(this.invitationCodesRepository).saveAll(userAC.capture());
        assertThat(userAC.getValue()).hasSize(10);
    }
}
