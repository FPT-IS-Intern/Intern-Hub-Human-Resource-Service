package com.fis.hrmservice.infra.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.fis.hrmservice.infra.feign")
public class FeignConfiguration {

  @Value("security.internal-secret")
  private String secretKey;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> requestTemplate.header("X-Internal-Secret", secretKey);
  }
}
