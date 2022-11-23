package io.tech1.framework.configurations.events;

import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationEventsTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class,
            ApplicationEvents.class
    })
    static class ContextConfiguration {

    }

    private final ApplicationEvents componentUnderTest;

    @Test
    public void simpleApplicationEventMulticasterTest() {
        // Act
        var actual = this.componentUnderTest.simpleApplicationEventMulticaster();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getClass()).isEqualTo(SimpleApplicationEventMulticaster.class);
    }
}
