package io.tech1.framework.foundation.domain.pubsub;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PubSubHierarchyTest {

    @Test
    void pubTest() {
        // Arrange
        var eventPublisher = new AbstractEventPublisher() {};

        // Ac
        var actual = eventPublisher.getType();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getValue()).isEqualTo("Pub");
        assertThat(actual.toString()).hasToString("Pub");
    }

    @Test
    void subTest() {
        // Arrange
        var eventSubscriber = new AbstractEventSubscriber() {};

        // Ac
        var actual = eventSubscriber.getType();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getValue()).isEqualTo("Sub");
        assertThat(actual.toString()).hasToString("Sub");
    }
}
