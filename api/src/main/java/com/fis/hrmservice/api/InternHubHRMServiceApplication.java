package com.fis.hrmservice.api;

import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.starter.security.annotation.EnableSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.fis.hrmservice", exclude = {
    UserDetailsServiceAutoConfiguration.class
})
@EnableJpaRepositories(basePackages = "com.fis.hrmservice.infra.persistence.repository")
@EntityScan(basePackages = "com.fis.hrmservice.infra.persistence.entity")
@EnableGlobalExceptionHandler
@EnableSecurity
@EnableScheduling
public class InternHubHRMServiceApplication {

  static void main(String[] args) {
    SpringApplication.run(InternHubHRMServiceApplication.class, args);
  }
}
