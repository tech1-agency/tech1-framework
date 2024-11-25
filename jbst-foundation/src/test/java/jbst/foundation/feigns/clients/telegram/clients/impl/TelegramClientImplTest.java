package jbst.foundation.feigns.clients.telegram.clients.impl;

import jbst.foundation.configurations.ConfigurationFeignClientTelegram;
import jbst.foundation.feigns.clients.telegram.clients.TelegramClient;
import jbst.foundation.feigns.clients.telegram.domain.requests.TelegramMessageRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TelegramClientImplTest {

    @Configuration
    @Import({
            ConfigurationFeignClientTelegram.class
    })
    static class TestConfiguration {

    }

    private final TelegramClient telegramClient;

    @Disabled
    @Test
    void sendMessage() {
        // Arrange
        var message = new TelegramMessageRequest(
                "<?>",
                "<?>",
                "<@username> <b>test-V4</b>"
        );

        // Act
        this.telegramClient.sendMessage(message);

        // Assert
        // no asserts
    }
}
