package io.tech1.framework.foundation.feigns.clients;

import feign.Request;
import feign.RetryableException;
import io.tech1.framework.foundation.domain.base.ServerName;
import io.tech1.framework.foundation.feigns.definitions.SpringBootClientFeign;
import io.tech1.framework.foundation.feigns.domain.spring.actuator.health.SpringBootActuatorHealth;
import io.tech1.framework.foundation.feigns.domain.spring.actuator.info.SpringBootActuatorInfo;
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
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;
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
        SpringBootClientFeign springBootClientFeign() {
            return mock(SpringBootClientFeign.class);
        }

        @Bean
        TestSpringBootClient testSpringBootClient() {
            return new TestSpringBootClient(
                    this.springBootClientFeign()
            );
        }
    }

    private final SpringBootClientFeign springBootClientFeign;

    private final TestSpringBootClient componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.springBootClientFeign
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.springBootClientFeign
        );
    }

    @Test
    void getServerNameTest() {
        // Act
        var serverName = this.componentUnderTest.getServerName();

        // Assert
        assertThat(serverName).isEqualTo(ServerName.testsHardcoded());
    }

    @Test
    void notAlive1Test() {
        // Act
        var alive = this.componentUnderTest.isAlive();

        // Assert
        verify(this.springBootClientFeign).info();
        assertThat(alive).isFalse();
    }

    @Test
    void notAlive2Test() {
        // Arrange
        when(this.springBootClientFeign.info()).thenThrow(RETRYABLE_EXCEPTION);

        // Act
        var alive = this.componentUnderTest.isAlive();

        // Assert
        verify(this.springBootClientFeign).info();
        assertThat(alive).isFalse();
    }

    @Test
    void aliveTest() {
        // Arrange
        when(this.springBootClientFeign.info()).thenReturn(SpringBootActuatorInfo.testsHardcoded());

        // Act
        var alive = this.componentUnderTest.isAlive();

        // Assert
        verify(this.springBootClientFeign).info();
        assertThat(alive).isTrue();
    }

    @Test
    void infoExceptionTest() {
        // Arrange
        when(this.springBootClientFeign.info()).thenThrow(RETRYABLE_EXCEPTION);

        // Act
        var info = this.componentUnderTest.info();

        // Assert
        verify(this.springBootClientFeign).info();
        assertThat(info).isEqualTo(SpringBootActuatorInfo.offline());
    }

    @Test
    void infoTest() {
        // Act
        var info = this.componentUnderTest.info();

        // Assert
        verify(this.springBootClientFeign).info();
        assertThat(info).isNull();
    }

    @Test
    void infoMappedByServerNameTest() {
        // Act
        var tuple2 = this.componentUnderTest.infoMappedByServerName();

        // Assert
        verify(this.springBootClientFeign).info();
        assertThat(tuple2.a()).isEqualTo(ServerName.testsHardcoded());
        assertThat(tuple2.b()).isNull();
    }

    @Test
    void healthExceptionTest() {
        // Arrange
        when(this.springBootClientFeign.health()).thenThrow(RETRYABLE_EXCEPTION);

        // Act
        var health = this.componentUnderTest.health();

        // Assert
        verify(this.springBootClientFeign).health();
        assertThat(health).isEqualTo(SpringBootActuatorHealth.unknown());
    }

    @Test
    void healthTest() {
        // Act
        var health = this.componentUnderTest.health();

        // Assert
        verify(this.springBootClientFeign).health();
        assertThat(health).isNull();
    }

    @Test
    void healthMappedByServerNameTest() {
        // Act
        var tuple2 = this.componentUnderTest.healthMappedByServerName();

        // Assert
        verify(this.springBootClientFeign).health();
        assertThat(tuple2.a()).isEqualTo(ServerName.testsHardcoded());
        assertThat(tuple2.b()).isNull();
    }
}
