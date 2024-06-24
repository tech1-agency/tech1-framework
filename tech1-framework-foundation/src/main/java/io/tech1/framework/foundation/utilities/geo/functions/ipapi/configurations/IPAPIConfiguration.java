package io.tech1.framework.foundation.utilities.geo.functions.ipapi.configurations;

import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.tech1.framework.foundation.utilities.geo.functions.ipapi.feign.IPAPIFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPAPIConfiguration {

    @Bean
    IPAPIFeign ipapiFeign() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .retryer(Retryer.NEVER_RETRY)
                .target(IPAPIFeign.class, "http://ip-api.com");
    }
}
