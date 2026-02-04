package com.fis.hrmservice.api.config;

import com.fis.hrmservice.domain.service.UserValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanConfig {

  @Bean
  public UserValidationService userValidationService() {
    return new UserValidationService();
  }
}
