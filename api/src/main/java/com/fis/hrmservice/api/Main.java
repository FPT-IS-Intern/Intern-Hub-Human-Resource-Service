package com.fis.hrmservice.api;

import com.intern.hub.library.common.autoconfig.context.ContextAutoConfiguration;
import com.intern.hub.library.common.autoconfig.snowflake.SnowflakeAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        SnowflakeAutoConfiguration.class,
        ContextAutoConfiguration.class
})
public class Main {
    static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}