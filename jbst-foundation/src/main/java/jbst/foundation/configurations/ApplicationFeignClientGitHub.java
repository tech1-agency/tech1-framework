package jbst.foundation.configurations;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import jbst.foundation.feigns.clients.github.clients.GithubClient;
import jbst.foundation.feigns.clients.github.clients.impl.GithubClientImpl;
import jbst.foundation.feigns.clients.github.definitions.GithubDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationFeignClientGitHub {

    @Bean
    GithubDefinition githubDefinition() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(GithubDefinition.class, "https://api.github.com");
    }

    @Bean
    GithubClient githubClient(GithubDefinition definition) {
        return new GithubClientImpl(definition);
    }
}
