package jbst.foundation.configurations;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import jbst.foundation.feigns.clients.openai.clients.OpenaiClient;
import jbst.foundation.feigns.clients.openai.clients.impl.OpenaiClientImpl;
import jbst.foundation.feigns.clients.openai.definions.OpenaiDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationFeignClientOpenai {

    @Bean
    OpenaiDefinition openaiDefinition() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OpenaiDefinition.class, "https://api.openai.com");
    }

    @Bean
    OpenaiClient openaiClient(OpenaiDefinition definition) {
        return new OpenaiClientImpl(definition);
    }
}
