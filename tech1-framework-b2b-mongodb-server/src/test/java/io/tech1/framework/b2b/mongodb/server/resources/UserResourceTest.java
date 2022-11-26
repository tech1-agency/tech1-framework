package io.tech1.framework.b2b.mongodb.server.resources;

import io.tech1.framework.b2b.mongodb.server.services.UserService;
import io.tech1.framework.b2b.mongodb.server.tests.runners.ApplicationResourceRunner;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResourceTest extends ApplicationResourceRunner {

    // Services
    private final UserService userService;

    private final UserResource resourceUnderTest;

    @BeforeEach
    public void beforeEach() {
        this.beforeByResource(this.resourceUnderTest);
        reset(
                this.userService
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.userService
        );
    }

    @Test
    public void findAll() throws Exception {
        // Act
        mvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert
        verify(this.userService).findAll();
    }
}
