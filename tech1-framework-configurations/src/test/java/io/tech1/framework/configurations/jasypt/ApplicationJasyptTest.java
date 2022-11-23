package io.tech1.framework.configurations.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationJasyptTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class,
            ApplicationJasypt.class
    })
    static class ContextConfiguration {

    }

    private final ApplicationJasypt componentUnderTest;

    @Test
    public void annotationTest() {
        // Assert
        assertThat(this.componentUnderTest).isNotNull();
        assertThat(ApplicationJasypt.class.isAnnotationPresent(EnableEncryptableProperties.class)).isTrue();
    }
}
