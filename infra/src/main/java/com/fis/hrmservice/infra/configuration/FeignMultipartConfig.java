package com.fis.hrmservice.infra.configuration;

import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.codec.Encoder;

@Configuration
public class FeignMultipartConfig {

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(); // ✅ OK
    }
}