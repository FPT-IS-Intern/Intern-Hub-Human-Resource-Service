package com.fis.hrmservice.api;

import com.intern.hub.library.common.autoconfig.context.ContextAutoConfiguration;
import com.intern.hub.library.common.autoconfig.snowflake.SnowflakeAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = "com.fis.hrmservice",
        exclude = {
                SnowflakeAutoConfiguration.class,
                ContextAutoConfiguration.class
        }
)
@EnableJpaRepositories(basePackages = "com.fis.hrmservice.infra.persistence.repository")
@EntityScan(basePackages = "com.fis.hrmservice.infra.persistence.entity")
public class Main {

    static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
