package jbst.foundation.configurations;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import jbst.foundation.feigns.clients.telegram.clients.TelegramClient;
import jbst.foundation.feigns.clients.telegram.clients.impl.TelegramClientImpl;
import jbst.foundation.feigns.clients.telegram.definitions.TelegramDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationFeignClientTelegram {

    @Bean
    TelegramDefinition telegramDefinition() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(TelegramDefinition.class, "https://api.telegram.org");
    }

    @Bean
    TelegramClient telegramClient(TelegramDefinition definition) {
        return new TelegramClientImpl(definition);
    }
}
