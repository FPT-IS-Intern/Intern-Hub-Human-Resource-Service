package com.fis.hrmservice.infra.persistence.config;

import com.intern.hub.library.common.utils.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {

  @Bean
  public Snowflake snowflake() {
    return new Snowflake(1L);
  }
}
