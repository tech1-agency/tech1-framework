package jbst.foundation.feigns.spring;

import feign.Request;
import feign.RetryableException;
import jbst.foundation.domain.base.ServerName;
import jbst.foundation.feigns.spring.domain.SpringBootActuatorHealth;
import jbst.foundation.feigns.spring.domain.SpringBootActuatorInfo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import static feign.Request.HttpMethod.GET;
import static jbst.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TestSpringBootClientTest {

    @SuppressWarnings("deprecation")
    private static final RetryableException RETRYABLE_EXCEPTION = new RetryableException(
            500,
            randomString(),
            GET,
            new Date(),
            Request.create(GET.toString(), randomString(), Map.of(), new byte[] {}, Charset.defaultCharset())
    );

    @Configuration
    static class ContextConfiguration {
        @Bean
        SpringBootDefinition springBootClientFeign() {
            return mock(SpringBootDefinition.class);
        }

        @Bean
        TestSpringBootClient testSpringBootClient() {
            return new TestSpringBootClient(
                    this.springBootClientFeign()
            );
        }
    }

    private final SpringBootDefinition definition;

    private final TestSpringBootClient componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.definition
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.definition
        );
    }

    @Test
    void getServerNameTest() {
        // Act
        var serverName = this.componentUnderTest.getServerName();

        // Assert
        assertThat(serverName).isEqualTo(ServerName.hardcoded());
    }

    @Test
    void notAlive1Test() {
        // Act
        var alive = this.componentUnderTest.isAlive();

        // Assert
        verify(this.definition).info();
        assertThat(alive).isFalse();
    }

    @Test
    void notAlive2Test() {
        // Arrange
        when(this.definition.info()).thenThrow(RETRYABLE_EXCEPTION);

        // Act
        var alive = this.componentUnderTest.isAlive();

        // Assert
        verify(this.definition).info();
        assertThat(alive).isFalse();
    }

    @Test
    void aliveTest() {
        // Arrange
        when(this.definition.info()).thenReturn(SpringBootActuatorInfo.hardcoded());

        // Act
        var alive = this.componentUnderTest.isAlive();

        // Assert
        verify(this.definition).info();
        assertThat(alive).isTrue();
    }

    @Test
    void infoExceptionTest() {
        // Arrange
        when(this.definition.info()).thenThrow(RETRYABLE_EXCEPTION);

        // Act
        var info = this.componentUnderTest.info();

        // Assert
        verify(this.definition).info();
        assertThat(info).isEqualTo(SpringBootActuatorInfo.offline());
    }

    @Test
    void infoTest() {
        // Act
        var info = this.componentUnderTest.info();

        // Assert
        verify(this.definition).info();
        assertThat(info).isNull();
    }

    @Test
    void infoMappedByServerNameTest() {
        // Act
        var tuple2 = this.componentUnderTest.infoMappedByServerName();

        // Assert
        verify(this.definition).info();
        assertThat(tuple2.a()).isEqualTo(ServerName.hardcoded());
        assertThat(tuple2.b()).isNull();
    }

    @Test
    void healthExceptionTest() {
        // Arrange
        when(this.definition.health()).thenThrow(RETRYABLE_EXCEPTION);

        // Act
        var health = this.componentUnderTest.health();

        // Assert
        verify(this.definition).health();
        assertThat(health).isEqualTo(SpringBootActuatorHealth.unknown());
    }

    @Test
    void healthTest() {
        // Act
        var health = this.componentUnderTest.health();

        // Assert
        verify(this.definition).health();
        assertThat(health).isNull();
    }

    @Test
    void healthMappedByServerNameTest() {
        // Act
        var tuple2 = this.componentUnderTest.healthMappedByServerName();

        // Assert
        verify(this.definition).health();
        assertThat(tuple2.a()).isEqualTo(ServerName.hardcoded());
        assertThat(tuple2.b()).isNull();
    }
}
