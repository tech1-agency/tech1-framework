package jbst.foundation.configurations;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import jbst.foundation.feigns.slack.SlackClient;
import jbst.foundation.feigns.slack.SlackDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationFeignClientSlack {

    @Bean
    SlackDefinition slackDefinition() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(SlackDefinition.class, "https://slack.com/api");
    }

    @Bean
    SlackClient slackClient() {
        return new SlackClient(this.slackDefinition());
    }
}
