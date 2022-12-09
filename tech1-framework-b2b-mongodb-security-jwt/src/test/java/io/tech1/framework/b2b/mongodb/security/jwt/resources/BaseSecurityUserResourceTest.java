package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseUserService;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.runnerts.AbstractResourcesRunner;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.BaseUserValidator;
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
public class BaseSecurityUserResourceTest extends AbstractResourcesRunner {

    // Services
    private final BaseUserService baseUserService;
    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Validators
    private final BaseUserValidator baseUserValidator;

    // Resource
    private final BaseSecurityUserResource componentUnderTest;

    @BeforeEach
    public void beforeEach() throws Exception {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.baseUserService,
                this.currentSessionAssistant,
                this.baseUserValidator
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.baseUserService,
                this.currentSessionAssistant,
                this.baseUserValidator
        );
    }

    @Test
    public void update1Test() throws Exception {
        // Arrange
        var requestUserUpdate1 = entity(RequestUserUpdate1.class);
        var currentDbUser = entity(DbUser.class);
        when(this.currentSessionAssistant.getCurrentDbUser()).thenReturn(currentDbUser);

        // Act
        this.mvc.perform(
                post("/user/update1")
                        .content(this.objectMapper.writeValueAsString(requestUserUpdate1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentDbUser();
        verify(this.baseUserValidator).validateUserUpdateRequest1(eq(currentDbUser), eq(requestUserUpdate1));
        verify(this.baseUserService).updateUser1(eq(requestUserUpdate1));
    }

    @Test
    public void update2Test() throws Exception {
        // Arrange
        var requestUserUpdate2 = entity(RequestUserUpdate2.class);

        // Act
        this.mvc.perform(
                        post("/user/update2")
                                .content(this.objectMapper.writeValueAsString(requestUserUpdate2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.baseUserValidator).validateUserUpdateRequest2(eq(requestUserUpdate2));
        verify(this.baseUserService).updateUser2(eq(requestUserUpdate2));
    }

    @Test
    public void changePassword1Test() throws Exception {
        // Arrange
        var requestUserChangePassword1 = entity(RequestUserChangePassword1.class);

        // Act
        this.mvc.perform(
                post("/user/changePassword1")
                        .content(this.objectMapper.writeValueAsString(requestUserChangePassword1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.baseUserValidator).validateUserChangePasswordRequest1(eq(requestUserChangePassword1));
        verify(this.baseUserService).changePassword1(eq(requestUserChangePassword1));
    }
}
