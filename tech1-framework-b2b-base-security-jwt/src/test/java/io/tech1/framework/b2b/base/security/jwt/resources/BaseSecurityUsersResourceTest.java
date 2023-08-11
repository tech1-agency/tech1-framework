package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersService;
import io.tech1.framework.b2b.base.security.jwt.tests.runners.AbstractResourcesRunner;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityUsersResourceTest extends AbstractResourcesRunner {

    // Services
    private final BaseUsersService baseUsersService;
    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Validators
    private final BaseUsersValidator baseUsersValidator;

    // Resource
    private final BaseSecurityUsersResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.baseUsersService,
                this.currentSessionAssistant,
                this.baseUsersValidator
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.baseUsersService,
                this.currentSessionAssistant,
                this.baseUsersValidator
        );
    }

    @Test
    void update1Test() throws Exception {
        // Arrange
        var requestUserUpdate1 = entity(RequestUserUpdate1.class);
        var jwtUser = entity(JwtUser.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(jwtUser);

        // Act
        this.mvc.perform(
                post("/users/update1")
                        .content(this.objectMapper.writeValueAsString(requestUserUpdate1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.baseUsersValidator).validateUserUpdateRequest1(jwtUser.username(), requestUserUpdate1);
        verify(this.baseUsersService).updateUser1(jwtUser, requestUserUpdate1);
    }

    @Test
    void update2Test() throws Exception {
        // Arrange
        var requestUserUpdate2 = entity(RequestUserUpdate2.class);
        var jwtUser = entity(JwtUser.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(jwtUser);

        // Act
        this.mvc.perform(
                        post("/users/update2")
                                .content(this.objectMapper.writeValueAsString(requestUserUpdate2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.baseUsersValidator).validateUserUpdateRequest2(requestUserUpdate2);
        verify(this.baseUsersService).updateUser2(jwtUser, requestUserUpdate2);
    }

    @Test
    void changePassword1Test() throws Exception {
        // Arrange
        var requestUserChangePassword1 = entity(RequestUserChangePassword1.class);
        var jwtUser = entity(JwtUser.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(jwtUser);

        // Act
        this.mvc.perform(
                post("/users/changePassword1")
                        .content(this.objectMapper.writeValueAsString(requestUserChangePassword1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.baseUsersValidator).validateUserChangePasswordRequest1(requestUserChangePassword1);
        verify(this.baseUsersService).changePassword1(jwtUser, requestUserChangePassword1);
    }
}
